import 'package:flutter/material.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:live_app/routes/app_pages.dart';

import '../../../utils/color_util.dart';
import '../../../widgets/cache_image.dart';
import 'create_order_logic.dart';

class CreateOrderPage extends BaseStatefulWidget<CreateOrderLogic> {
  const CreateOrderPage({super.key}) ;

  @override
  String? title() {
    // TODO: implement title
    return '创建订单';
  }

  @override
  Widget buildContent(BuildContext context) {
    return _buildView();
  }

  // 主视图
  Widget _buildView() {
    return SafeArea(
        child: Column(
          children: [
            Expanded(
              child: SingleChildScrollView(
                padding: const EdgeInsets.symmetric(vertical: 10.0, horizontal: 10.0),
                child: Column(
                    children: [
                      _buildAddressView(),
                      const SizedBox(height: 10.0),
                      _buildGoodsView(),
                      const SizedBox(height: 10.0),
                      _buildOtherView(),
                      const SizedBox(height: 10.0),
                      _buildPaymentView()
                    ]
                ),
              ),
            ),
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 10.0),
              decoration: const BoxDecoration(
                color: ColorUtil.white,
              ),
              child: Row(
                children: [
                  Text("实际支付：￥${controller.totalPrice.value}", style: TextStyle(fontSize: 16.0, color: ColorUtil.primaryColor, fontWeight: FontWeight.bold)),
                  const Spacer(),
                  TextButton(
                    onPressed: controller.onSubmit,
                    style: TextButton.styleFrom(
                      // foregroundColor: ColorUtil.white,
                        backgroundColor: ColorUtil.primaryColor,
                        padding: const EdgeInsets.symmetric(horizontal: 20.0, vertical: 8.0),
                        shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(40.0)
                        )
                    ),
                    child: const Text("立即支付", style: TextStyle(fontSize: 18.0, color: ColorUtil.white)),
                  ),
                ],
              ),
            )
          ],
        )
    );
  }

  // 收货地址
  Widget _buildAddressView() {
    return InkWell(
      onTap: () {
        Get.toNamed(AppPages.addressListPage)?.then((result) {
          if (result != null) {
            controller.onSelectAddress(result);
          }
        });
      },
      child: Container(
          padding: const EdgeInsets.symmetric(horizontal: 15.0, vertical: 10.0),
          decoration: const BoxDecoration(
              color: ColorUtil.white,
              borderRadius: BorderRadius.all(Radius.circular(8.0))
          ),
          child: Row(
            children: [
              Expanded(
                child: controller.address.value.id.isEmpty
                    ? const Padding(padding: EdgeInsets.symmetric(vertical: 20.0), child: Text("请选择收货地址", style: TextStyle(color: ColorUtil.black, fontSize: 15.0)))
                    : Obx(() => Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text("${controller.address.value.provinceName}${controller.address.value.cityName}${controller.address.value.countiesName}", style: const TextStyle(color: ColorUtil.black, fontSize: 15.0)),
                      const SizedBox(height: 10.0),
                      Text(controller.address.value.address, style: const TextStyle(color: ColorUtil.black, fontSize: 18.0, fontWeight: FontWeight.bold)),
                      const SizedBox(height: 10.0),
                      Text("${controller.address.value.nickname} ${controller.address.value.mobile}", style: const TextStyle(color: ColorUtil.black, fontSize: 15.0),)
                    ]
                )),
              ),
              const SizedBox(width: 20.0),
              const Icon(Icons.arrow_forward_ios, color: ColorUtil.black12, size: 20.0),
            ],
          )
      ),
    );
  }

  // 订单商品
  Widget _buildGoodsView() {
    return Container(
        padding: EdgeInsets.symmetric(horizontal: 15.w, vertical: 10.0),
        decoration: const BoxDecoration(
            color: ColorUtil.white,
            borderRadius: BorderRadius.all(Radius.circular(8.0))
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                ClipRRect(
                  borderRadius: BorderRadius.circular(8.0),
                  child: CacheImage(
                    controller.params['cover']??'',
                    width: 100.w,
                    height: 100.w,
                  ),
                ),
                SizedBox(width: 8.w),
                Expanded(
                  child: SizedBox(
                    height: 100.w,
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      mainAxisAlignment: MainAxisAlignment.start,
                      children: [
                        SizedBox(height: 8.w),
                        Text(controller.params['title']??'', style: TextStyle(fontSize: 16.0, color: ColorUtil.black), maxLines: 3, overflow: TextOverflow.ellipsis),
                      ],
                    ),
                  ),
                ),
                SizedBox(width: 10.w),
                Column(
                  children: [
                    Text("¥${controller.params['price']}", style: TextStyle(fontSize: 16.0, color: ColorUtil.primaryColor, fontWeight: FontWeight.bold)),
                    Obx(() => Text("${controller.quantity.value}件", style: TextStyle(fontSize: 15.0, color: ColorUtil.black))),
                  ],
                )
              ],
            )
          ],
        )
    );
  }

  Widget _buildOtherView() {
    return Container(
        padding: const EdgeInsets.symmetric(horizontal: 15.0, vertical: 10.0),
        decoration: const BoxDecoration(
            color: ColorUtil.white,
            borderRadius: BorderRadius.all(Radius.circular(8.0))
        ),
        child: Column(
            children: [
              Row(
                children: [
                  const Expanded(child: Text("数量", style: TextStyle(fontSize: 16.0, color: ColorUtil.black),)),
                  // Text("限购100件", style: TextStyle(fontSize: 14.0, color: ColorUtil.black45),),
                  InkWell(
                      onTap: () => controller.onGoodsQuantityChange(-1),
                      child: Container(
                        width: 24.w,
                        height: 24.w,
                        alignment: Alignment.center,
                        decoration: const BoxDecoration(
                            color: ColorUtil.backgroundColor
                        ),
                        child: Text("-", style: TextStyle(fontSize: 18.sp, color: ColorUtil.black54)),
                      )
                  ),
                  SizedBox(
                      width: 40.0,
                      height: 40.0,
                      child: Center(
                        child: Obx(() => Text("${controller.quantity.value}", style: TextStyle(fontSize: 16.0, color: ColorUtil.primaryColor))),
                      )
                  ),
                  InkWell(
                      onTap: () => controller.onGoodsQuantityChange(1),
                      child: Container(
                        width: 24.w,
                        height: 24.w,
                        decoration: const BoxDecoration(
                            color: ColorUtil.backgroundColor
                        ),
                        child: Icon(Icons.add, size: 20.sp, color: ColorUtil.black54,),
                      )
                  )
                ],
              ),
              const SizedBox(height: 10.0),
              // 备注
              Row(
                children: [
                  const Text("备注", style: TextStyle(fontSize: 16.0, color: ColorUtil.black)),
                  const SizedBox(width: 20.0),
                  Expanded(
                    child: TextField(
                      controller: controller.remarkController,
                      textAlign: TextAlign.end,
                      decoration: const InputDecoration(
                        border: InputBorder.none,
                        hintText: "请输入备注",
                        hintStyle: TextStyle(fontSize: 14.0, color: ColorUtil.black45),
                      ),
                    ),
                  ),
                ],
              )
            ]
        )
    );
  }

  Widget _buildPaymentView() {
    return Container(
        decoration: const BoxDecoration(
            color: ColorUtil.white,
            borderRadius: BorderRadius.all(Radius.circular(8.0))
        ),
        child: Column(
            children: controller.paymentList.map((item) {
              return ListTile(
                leading: ClipRRect(
                    borderRadius: const BorderRadius.all(Radius.circular(8.0)),
                    child: CacheImage(
                      item.icon,
                      width: 30.0,
                      height: 30.0,
                    )
                ),
                title: Text(item.title, style: const TextStyle(fontSize: 16.0, color: ColorUtil.primaryColor),),
                trailing: controller.payType.value == item.type
                    ? const Icon(Icons.check_circle_outline, color: ColorUtil.primaryColor, size: 24.0)
                    : const Icon(Icons.circle_outlined, size: 24.0, color: ColorUtil.black26),
                onTap: () {
                  controller.onSelectPayment(item.type);
                },
              );
            }).toList()
        )
    );
  }

}
