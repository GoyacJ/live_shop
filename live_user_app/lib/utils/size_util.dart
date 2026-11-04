
class SizeUtil{

  static int convertRatio = 1024 * 1024 * 1024;

  /// 常见存储容量的字节数列表，按二进制计算
  static Map<int, String> storageCapacities = {
    32 * convertRatio: "32 GB",
    64 * convertRatio: "64 GB",
    128 * convertRatio: "128 GB",
    256 * convertRatio: "256 GB",
    512 * convertRatio: "512 GB",
    1024 * convertRatio: "1 TB",
    2048 * convertRatio: "2 TB",
    4096 * convertRatio: "4 TB",
    6144 * convertRatio: "6 TB",
    8192 * convertRatio: "8 TB",
  };
  static String estimateStorageCapacity(int totalBytes) {

    // 找到第一个大于或等于 totalBytes 的存储容量
    for (int capacity in storageCapacities.keys) {
      if (totalBytes <= capacity) {
        return storageCapacities[capacity]!;
      }
    }

    // 如果 totalBytes 超过已知的值，则返回最大的可用容量
    return storageCapacities[storageCapacities.keys.last]!;
  }

  /// 格式化文件大小，把字节转换为 B、KB、MB、GB、TB，保留两位小数
  static String formatFileSize(int size, {int fixed = 2, bool isBytes = true,bool isRemoveEndZeros = false}) {
    size = size.abs();
    if (size < 1024 && isBytes) {
      return "$size B";
    } else if (size < 1024 * 1024) {
      return "${removeEndZeros((size / 1024).toStringAsFixed(fixed),isRemoveEndZeros: isRemoveEndZeros)} KB";
    } else if (size < 1024 * 1024 * 1024) {
      return "${removeEndZeros((size / 1024 / 1024).toStringAsFixed(fixed),isRemoveEndZeros: isRemoveEndZeros)} MB";
    } else if (size < 1024 * 1024 * 1024 * 1024) {
      return "${removeEndZeros((size / 1024 / 1024 / 1024).toStringAsFixed(fixed),isRemoveEndZeros: isRemoveEndZeros)} GB";
    } else {
      return "${removeEndZeros((size / 1024 / 1024 / 1024 / 1024).toStringAsFixed(fixed),isRemoveEndZeros: isRemoveEndZeros)} TB";
    }
  }

  ///移除数字末尾的0
  static String removeEndZeros(String number,{bool isRemoveEndZeros = false}) {
    if(!isRemoveEndZeros){
      return number;
    }
    if (number.contains('.')) {
      number = number.replaceAll(RegExp(r'0*$'), ''); // 去掉末尾的0
      number = number.replaceAll(RegExp(r'\.$'), ''); // 如果最后是.，去掉它
    }
    return number;
  }

  ///通过storageCapacities的value获取key
  static int getStorageCapacityKey(String value){
    for(int key in storageCapacities.keys){
      if(storageCapacities[key] == value){
        return key;
      }
    }
    return 0;
  }
}