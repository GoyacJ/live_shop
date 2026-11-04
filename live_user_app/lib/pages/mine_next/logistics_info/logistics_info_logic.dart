import 'package:get/get.dart';
import 'package:live_app/base/controller/base_controller.dart';
import 'package:live_app/network/net_util.dart';
import 'package:live_app/utils/log_utils.dart';

import '../../../models/logistice_model.dart';
String tag = 'LogisticsInfoLogic';
class LogisticsInfoLogic extends BaseController {
  LogisticsModel? model;
  String? orderNumber;

  @override
  initData()async {
    // TODO: implement initData
    orderNumber = Get.arguments;
    getData();
  }

  getData()async{
    try{
      showEasyLoading();
      Map<String, dynamic> result = await NetUtil.getLogisticsInfo(orderNumber);
      model = LogisticsModel.fromJson(result['data']);
      dismissEasyLoading();
      update();
    }catch(e){
      LogUtils.d('$tag--------------------$e');
      dismissEasyLoading();
    }
  }
}
