import 'package:common_utils/common_utils.dart';
import 'package:live_app/base/controller/base_controller.dart';
import 'package:live_app/services/user_info_ctl.dart';
import 'package:live_app/utils/toast_util.dart';
import 'package:get/get.dart';
import 'package:image_picker/image_picker.dart';

import '../../../utils/permission_util.dart';

class UpdateAvatarLogic extends BaseController {

  final imageUrl = 'assets/images/default-avatar.jpg'.obs;

  @override
  initData() async{
    // TODO: implement initData
    if(ObjectUtil.isNotEmpty(UserInfoCtl.to.profile.avatar)){
      imageUrl.value = UserInfoCtl.to.profile.avatar;
    }
    PermissionUtil.requestAll();
  }

  onSelectImage() async {
    ImagePicker picker = ImagePicker();
    XFile? result = await picker.pickImage(
      source: ImageSource.gallery,
      imageQuality: 100,
    );
    if (result != null) {
      imageUrl.value = result.path;
      print("上传图片结果：${result.name}-----${result.path}");
      print(result.toString());
      int fileSize = await result.length();
      if ( fileSize > 5242880) {
        ToastUtil.showToast("图片大小不能超过5M");
        return ;
      } else {
        print("大小：$fileSize");
      }

    }
  }

  saveData(){
    // uploadFile();
  }

  // Future<void> uploadFile() async {
  //   Dio dio = Dio();
  //
  //   String path = imageUrl.value;
  //   String fileName = path.split('/').last;
  //
  //   FormData formData = FormData.fromMap({
  //     "avatar": await MultipartFile.fromFile(
  //       path,
  //       filename: fileName,
  //       contentType: DioMediaType("image", "jpeg"), // 如果你的是 PNG 改为 image/png
  //     ),
  //   });
  //
  //   try {
  //     dio.options.headers['Content-Type'] =
  //     'application/x-www-form-urlencoded';
  //     final response = await dio.post(
  //       "https://sp.zhiqinxingchen.com/open/api/upload", // 👈 使用接口文档里的地址
  //       data: formData,
  //     );
  //
  //     print("✅ 上传成功: ${response.data}");
  //   } catch (e) {
  //     if (e is DioException && e.response != null) {
  //       print("❌ 上传失败: 状态码 ${e.response!.statusCode}");
  //       print("❌ 响应数据: ${e.response!.data}");
  //     } else {
  //       print("❌ Dio 异常: $e");
  //     }
  //   }
  // }
}
