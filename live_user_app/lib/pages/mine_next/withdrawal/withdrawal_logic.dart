import 'package:live_app/base/controller/base_controller.dart';
import 'package:live_app/network/net_util.dart';
import 'package:live_app/utils/config_util.dart';
import 'package:get/get.dart';

import '../../../models/draw_amount.dart';

class WithdrawalLogic extends BaseController {
  RxList<DrawAmountEntity> amountList = <DrawAmountEntity>[].obs;
  // RxList<DrawAccountEntity> amountList = <DrawAccountEntity>[].obs;
  List withdrawalMethod = [];
  // int selectedMethod = 999;
  RxInt selectedMethod = 999.obs;


  @override
  initData()async{
    withdrawalMethod = [
      // {
      //   "title": "微信",
      //   "account": "",
      //   "account_id": 0,
      //   "icon": "https://pic.rmb.bdstatic.com/cms/f2bd7dd9db0285fcb407408443ddda87_1686187779.193_825.png",
      // },
      {
        "title": "支付宝",
        "account": "188****2606",
        "account_id": 1,
        "icon": "assets/images/ic_ali.png",
      },
      // {
      //   "title": "银行卡",
      //   "account": "中国银行(4013)",
      //   "account_id": 1,
      //   "icon": "https://pic.rmb.bdstatic.com/cms/f2bd7dd9db0285fcb407408443ddda87_1686187779.193_825.png",
      // }
    ];
    _initData();
  }

  _initData() async {
    final Map<String, dynamic> result = await NetUtil.getWithdrawalAmount();
    if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
      amountList.value = result['data']['available'].map<DrawAmountEntity>((item) {
        return DrawAmountEntity.fromJson(item);
      }).toList();
    }
    update();
  }


  void onSelectedMethod(int index) {
    // 判断有没有绑定提现账号
    selectedMethod.value = index;
    update();
  }
}
