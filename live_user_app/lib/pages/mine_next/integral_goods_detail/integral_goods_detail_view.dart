import 'package:flutter/material.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:live_app/routes/app_pages.dart';
import 'package:live_app/utils/color_util.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:live_app/widgets/jh_text.dart';
import '../../../widgets/cache_image.dart';
import 'integral_goods_detail_logic.dart';

class IntegralGoodsDetailPage extends BaseStatefulWidget<IntegralGoodsDetailLogic> {
  const IntegralGoodsDetailPage({super.key});

  @override
  String? title() {
    // TODO: implement title
    return '商品详情';
  }

  @override
  Widget buildContent(BuildContext context) {
    return  _buildView();
  }

  // 主视图
  Widget _buildView() {
    return Column(
      children: [
        Expanded(
          child: SingleChildScrollView(
            child: Column(
              children: [
                _buildSlider(),
                _buildInfo(),
                _buildAddress(),
                _buildDetail(),
                const SizedBox(height: 20.0)
              ],
            ),
          ),
        ),

        _buildBottom(),
      ],
    );
  }

  // 顶部轮播图
  Widget _buildSlider() {
    return Stack(
      children: [
        CacheImage(
          controller.goodsDetail.value.cover,
          height: 375.w,
        ),
        Positioned(
          right: 8.0,
          bottom: 10.0,
          child: Container(
            height: 24.0,
            padding: const EdgeInsets.symmetric(horizontal: 16),
            decoration: BoxDecoration(
                color: ColorUtil.black50,
                borderRadius: BorderRadius.circular(12)
            ),
            child: Center(
              child: JhText.cText("1 / 1", ColorUtil.white, 15.0),
            ),
          ),
        )
      ],
    );
  }

  Widget _buildInfo() {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 20.0),
      child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            JhText.cText(controller.goodsDetail.value.title, ColorUtil.black, 18.0, fontWeight: FontWeight.bold),
            const SizedBox(height: 5),
            Row(
              crossAxisAlignment: CrossAxisAlignment.end,
              children: [
                JhText.cText("${controller.goodsDetail.value.integral}", ColorUtil.primaryColor, 16.0, fontWeight: FontWeight.bold),
                JhText.cText("积分", ColorUtil.primaryColor, 13.0),
                const SizedBox(width: 12.0),
                Text("原价：${controller.goodsDetail.value.marketPrice}元", style:const TextStyle(color: ColorUtil.black54, fontSize: 13.0, decoration: TextDecoration.lineThrough, decorationColor: ColorUtil.black54), ),
                const Spacer(),
                JhText.cText("库存：${controller.goodsDetail.value.stock}", ColorUtil.black, 13.0),
              ],
            ),
          ]
      ),
    );
  }

  Widget _buildAddress() {
    return Container(
      margin: EdgeInsets.symmetric(horizontal: 16.0),
      padding: EdgeInsets.symmetric(vertical: 20.0),
      decoration: BoxDecoration(
        border: Border(
          top: BorderSide(color: ColorUtil.black12, width: 1),
          bottom: BorderSide(color: ColorUtil.black12, width: 1),
        ),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          InkWell(
            onTap: () {
              Get.toNamed(AppPages.addressListPage)?.then((result) {
                if (result != null) {
                  controller.onSelectAddress(result);
                }
              });
            },
            child: Row(
              children: [
                JhText.cText(controller.address.value.mobile.isNotEmpty ? "更换地址" : "请选择地址", ColorUtil.black, 16.0),
                const Spacer(),
                Row(
                  children: [
                    JhText.cText("选择", ColorUtil.black, 16.0),
                    Icon(Icons.arrow_forward_ios, color: ColorUtil.black54, size: 16.0),
                  ],
                ),
              ],
            ),
          ),
          if (controller.address.value.mobile.isNotEmpty)
            Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const SizedBox(height: 10.0),
                  JhText.cText("${controller.address.value.provinceName} ${controller.address.value.cityName} ${controller.address.value.countiesName} ${controller.address.value.address}", ColorUtil.black, 16.0),
                  // const SizedBox(height: 6.0),
                  JhText.cText("${controller.address.value.nickname} ${controller.address.value.mobile}", ColorUtil.black, 16.0),
                ]
            )
        ],
      ),
    );
  }

  Widget _buildDetail() {
    return Padding(padding: EdgeInsets.symmetric(horizontal: 16.0, vertical: 10.0),
      child: JhText.cText(controller.goodsDetail.value.detail, ColorUtil.black, 14.0));
  }

  Widget _buildBottom() {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 10.0),
      color: ColorUtil.white,
      child: Row(
        children: [
          InkWell(
            onTap: () {
              Get.toNamed(AppPages.contactPage);
            },
            child: Column(
              children: [
                Icon(Icons.chat, color: ColorUtil.black54, size: 20.0),
                SizedBox(width: 4.0),
                JhText.cText("客服", ColorUtil.black54, 14.0)
              ],
            ),
          ),
          const Spacer(),
          InkWell(
            onTap: controller.onSubmit,
            child: Container(
              height: 40.0,
              alignment: Alignment.center,
              padding: const EdgeInsets.symmetric(horizontal: 16.0),
              decoration: BoxDecoration(
                color: ColorUtil.primaryColor,
                borderRadius: BorderRadius.circular(8.0),
              ),
              child: JhText.cText("立即兑换", ColorUtil.white, 16.0),
            ),
          ),
        ],
      ),
    );
  }
}
