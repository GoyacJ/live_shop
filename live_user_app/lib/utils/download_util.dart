import 'dart:io';
import 'package:dio/dio.dart';
import 'package:flutter/foundation.dart';
import 'package:live_app/utils/http_util.dart';
import 'package:live_app/utils/platform_util.dart';
import 'package:path_provider/path_provider.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:device_info_plus/device_info_plus.dart';
import 'log_utils.dart';

class DownloadTask {
  final String url;
  final String fileName;
  final ValueNotifier<double> progress;
  final ValueNotifier<DownloadStatus> status;
  final CancelToken cancelToken;

  DownloadTask({
    required this.url,
    required this.fileName,
    double initialProgress = 0.0,
    DownloadStatus initialStatus = DownloadStatus.pending,
  })  : progress = ValueNotifier(initialProgress),
        status = ValueNotifier(initialStatus),
        cancelToken = CancelToken();
}

enum DownloadStatus {
  pending,
  downloading,
  paused,
  completed,
  failed,
}

class DownloadUtils {
  static final DownloadUtils _instance = DownloadUtils._internal();

  factory DownloadUtils() => _instance;

  DownloadUtils._internal();

  static DownloadUtils get instance => _instance;

  final List<DownloadTask> _downloadTasks = [];

  List<DownloadTask> get downloadTasks => List.unmodifiable(_downloadTasks);

  /// 检查存储权限
  /// Android 13及以上版本使用细粒度的媒体权限
  /// Android 13以下版本使用存储权限
  /// iOS不需要存储权限
  Future<bool> checkStoragePermission() async {
    if (!Platform.isAndroid) return true;

    // 获取Android版本
    final deviceInfo = await PlatformUtil.getDeviceInfo();
    final sdkInt = deviceInfo.version.sdkInt;

    if (sdkInt >= 33) {
      // Android 13及以上版本
      // 检查照片和视频权限
      final photos = await Permission.photos.status;
      if (photos.isDenied) {
        final result = await Permission.photos.request();
        if (!result.isGranted) return false;
      }

      // 检查音频权限
      final audio = await Permission.audio.status;
      if (audio.isDenied) {
        final result = await Permission.audio.request();
        if (!result.isGranted) return false;
      }

      // 检查下载文件夹权限
      final downloads = await Permission.manageExternalStorage.status;
      if (downloads.isDenied) {
        final result = await Permission.manageExternalStorage.request();
        if (!result.isGranted) return false;
      }

      return true;
    } else {
      // Android 13以下版本
      final status = await Permission.storage.status;
      if (status.isDenied) {
        final result = await Permission.storage.request();
        return result.isGranted;
      }
      return status.isGranted;
    }
  }

  /// 获取下载路径
  Future<String> getDownloadPath() async {
    final directory = await getApplicationDocumentsDirectory();
    final downloadPath = '${directory.path}/downloads';
    final dir = Directory(downloadPath);
    if (!dir.existsSync()) {
      dir.createSync(recursive: true);
    }
    return downloadPath;
  }

  /// 添加下载任务
  Future<DownloadTask?> addDownloadTask(String url, {String? fileName}) async {
    try {
      if (!await checkStoragePermission()) {
        LogUtils.e('存储权限未授予');
        return null;
      }

      fileName ??= url.split('/').last;
      final downloadPath = await getDownloadPath();
      final task = DownloadTask(url: url, fileName: fileName);

      _downloadTasks.add(task);

      final savePath = '$downloadPath/${task.fileName}';
      task.status.value = DownloadStatus.downloading;

      await HttpUtil.instance.download(
        url,
        savePath,
        onReceiveProgress: (received, total) {
          if (total != -1) {
            task.progress.value = received / total;
          }
        },
        cancelToken: task.cancelToken,
      );

      task.status.value = DownloadStatus.completed;
      return task;
    } catch (e) {
      LogUtils.e('下载任务添加失败: $e');
      return null;
    }
  }

  /// 暂停下载
  void pauseDownload(DownloadTask task) {
    if (task.status.value == DownloadStatus.downloading) {
      task.cancelToken.cancel('用户暂停下载');
      task.status.value = DownloadStatus.paused;
    }
  }

  /// 恢复下载
  Future<void> resumeDownload(DownloadTask task) async {
    if (task.status.value == DownloadStatus.paused) {
      final newTask = await addDownloadTask(task.url, fileName: task.fileName);
      if (newTask != null) {
        final index = _downloadTasks.indexOf(task);
        _downloadTasks[index] = newTask;
      }
    }
  }

  /// 取消下载
  void cancelDownload(DownloadTask task) {
    task.cancelToken.cancel('用户取消下载');
    _downloadTasks.remove(task);
  }

  /// 清除已完成的任务
  void clearCompletedTasks() {
    _downloadTasks
        .removeWhere((task) => task.status.value == DownloadStatus.completed);
  }

  /// 获取指定URL的下载任务
  DownloadTask? getTaskByUrl(String url) {
    return _downloadTasks.firstWhere(
      (task) => task.url == url,
      orElse: () => null as DownloadTask,
    );
  }

  /// 获取正在下载的任务列表
  List<DownloadTask> getDownloadingTasks() {
    return _downloadTasks
        .where(
          (task) => task.status.value == DownloadStatus.downloading,
        )
        .toList();
  }

  /// 获取已完成的任务列表
  List<DownloadTask> getCompletedTasks() {
    return _downloadTasks
        .where(
          (task) => task.status.value == DownloadStatus.completed,
        )
        .toList();
  }

  /// 暂停所有下载任务
  void pauseAllTasks() {
    for (var task in _downloadTasks) {
      if (task.status.value == DownloadStatus.downloading) {
        pauseDownload(task);
      }
    }
  }

  /// 恢复所有暂停的任务
  Future<void> resumeAllTasks() async {
    for (var task in _downloadTasks.toList()) {
      if (task.status.value == DownloadStatus.paused) {
        await resumeDownload(task);
      }
    }
  }

  /// 取消所有任务
  void cancelAllTasks() {
    for (var task in _downloadTasks.toList()) {
      cancelDownload(task);
    }
  }

  /// 检查是否存在指定文件名的任务
  bool hasTaskWithFileName(String fileName) {
    return _downloadTasks.any((task) => task.fileName == fileName);
  }

  /// 获取下载任务数量
  int get taskCount => _downloadTasks.length;

  /// 获取正在下载的任务数量
  int get downloadingTaskCount => getDownloadingTasks().length;

  /// 获取已完成的任务数量
  int get completedTaskCount => getCompletedTasks().length;
}
