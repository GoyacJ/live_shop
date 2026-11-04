import 'package:common_utils/common_utils.dart';
import 'package:flutter/material.dart';
import 'package:live_app/routes/app_pages.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:live_app/utils/toast_util.dart';
import 'package:live_app/widgets/jh_text.dart';

import '../../../base/widget/base_stateful_widget.dart';
import '../../../utils/color_util.dart';
import '../../../widgets/my_list_tile.dart';
import 'order_detail_logic.dart';

class OrderDetailPage extends BaseStatefulWidget<OrderDetailLogic> {
  const OrderDetailPage({super.key});

  @override
  String? title() {
    // TODO: implement title
    return '订单详情';
  }

  @override
  Widget buildContent(BuildContext context) {
    return controller.orderDetail.isNotEmpty
            ? _buildView()
            : Center(child: JhText.cText("暂无数据", ColorUtil.black, 16.0));
  }

  // 主视图
  Widget _buildView() {
    return Column(
      children: [
        Expanded(
          child: SingleChildScrollView(
            padding:
                const EdgeInsets.symmetric(vertical: 10.0, horizontal: 10.0),
            child: Container(
              padding:
                  const EdgeInsets.symmetric(horizontal: 15.0, vertical: 10.0),
              decoration: const BoxDecoration(
                  color: ColorUtil.white,
                  borderRadius: BorderRadius.all(Radius.circular(8.0))),
              child: Column(
                children: [
                  // _buildOrderStatus(),
                  orders(controller.orderDetail["status"]),
                  const Divider(height: 1.0, color: ColorUtil.backgroundColor),
                  const SizedBox(height: 16.0),
                  // 商户/直播间标题
                  // Row(
                  //     children: [
                  //       Container(
                  //           margin: const EdgeInsets.only(right: 6.0),
                  //           padding: const EdgeInsets.symmetric(horizontal: 6.0,),
                  //           decoration: const BoxDecoration(
                  //               color: ColorUtil.primaryColor,
                  //               borderRadius: BorderRadius.all(Radius.circular(4.0))
                  //           ),
                  //           child: Text("直播间", style: const TextStyle(color: ColorUtil.white, fontSize: 14.0))
                  //       ),
                  //       Text("颐心相伴", style: const TextStyle(color: ColorUtil.black, fontSize: 15.0))
                  //     ]
                  // ),
                  // 商品信息
                  Padding(
                    padding: const EdgeInsets.symmetric(vertical: 10.0),
                    child: Row(
                      children: [
                        Expanded(
                          child: JhText.cText(
                              controller.orderDetail["goods"]["goodsTitle"],
                              ColorUtil.black, 18.0),
                        ),
                        const SizedBox(
                          width: 10.0,
                        ),
                        JhText.cText(
                            controller.orderDetail["goods"]["goodsPrice"]
                                .toString(),
                            ColorUtil.black, 16.0),
                      ],
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.symmetric(vertical: 10.0),
                    child: Row(
                      children: [
                        Expanded(
                          child: JhText.cText("商品数量",
                              ColorUtil.black, 18.0),
                        ),
                        const SizedBox(
                          width: 10.0,
                        ),
                        JhText.cText(controller.orderDetail["goods"]["goodsCount"],
                            ColorUtil.black, 16.0),
                      ],
                    ),
                  ),
                  // Row(
                  //   crossAxisAlignment: CrossAxisAlignment.start,
                  //   children: [
                  //     ClipRRect(
                  //       borderRadius: BorderRadius.circular(8.0),
                  //       child: Image.network("https://s2-10623.kwimgs.com/kimg/bs2/zt-image-host/ChgwOGEzZDQ5YmM2MDMxMGUyOTM5OGM4MDMQ3svXLw.jpg", width: 80.0, height: 80.0, fit: BoxFit.cover,),
                  //     ),
                  //     const SizedBox(width: 8.0,),
                  //     Expanded(
                  //       child: Column(
                  //         children: [
                  //           Text("商品名称 很长很长很长很长很长很长很长", style: const TextStyle(color: ColorUtil.black, fontSize: 15.0), maxLines: 2, overflow: TextOverflow.ellipsis,),
                  //           Text("数量 x1", style: const TextStyle(color: ColorUtil.black12, fontSize: 15.0)),
                  //
                  //         ],
                  //       ),
                  //     ),
                  //     const SizedBox(width: 8.0,),
                  //     Column(
                  //       children: [
                  //         // Text("￥${widget.order['price']}", style: const TextStyle(color: ColorUtil.black, fontSize: 15.0)),
                  //         Row(
                  //           children: [
                  //             Text("￥", style: const TextStyle(color: ColorUtil.black, fontSize: 15.0,)),
                  //             Text("29.9", style: const TextStyle(color: ColorUtil.black, fontSize: 18.0,))
                  //           ],
                  //         ),
                  //         Text("数量 x1", style: const TextStyle(color: ColorUtil.black12, fontSize: 15.0)),
                  //       ],
                  //     )
                  //   ],
                  // ),
                  // 订单其他信息
                  MyListTile(
                      title: "实付款",
                      value: "￥${controller.orderDetail["orderFee"]}"),
                  MyListTile(
                      title: "订单编号",
                      value: controller.orderDetail["orderNumber"]),
                  // 展开更多信息
                  // InkWell(
                  //   onTap: () {},
                  //   child: Row(
                  //     mainAxisAlignment: MainAxisAlignment.center,
                  //     children: [
                  //       Text("全部订单信息", style: const TextStyle(color: ColorUtil.black26, fontSize: 16.0),),
                  //       const Icon(Icons.keyboard_arrow_down_outlined, size: 20.0, color: ColorUtil.black26,)
                  //     ],
                  //   ),
                  // ),
                  // 订单全部信息
                  Row(
                    children: [
                      JhText.cText('物流信息',
                          ColorUtil.black, 18.0),
                      const SizedBox(width: 20.0),
                      Expanded(
                          child: InkWell(
                        child: Container(
                          padding: const EdgeInsets.symmetric(vertical: 10.0),
                          child: JhText.cText(
                            '查看',
                            ColorUtil.primaryColor, 16.0, textAlign: TextAlign.right,
                          ),
                        ),
                        onTap: () {
                          Get.toNamed(AppPages.logisticsInfoPage,arguments: controller.orderDetail["orderNumber"]);
                        },
                      ))
                    ],
                  ),
                  SizedBox(
                    child: Column(
                      children: [
                        MyListTile(title: "支付方式", value: "微信"),
                        // MyListTile(title: "支付时间", value: controller.orderDetail["createTime"]),
                        MyListTile(
                            title: "下单时间",
                            value: controller.orderDetail["createTime"]),
                        const Divider(
                          height: 10.0,
                          thickness: 0.4,
                          color: ColorUtil.black12,
                        ),
                        // MyListTile(title: "配送方式", value: "顺丰快递"),
                        MyListTile(
                            title: "收货信息",
                            value:
                                "${controller.orderDetail["nickname"]} ${controller.orderDetail["mobile"]}"),
                        MyListTile(
                            title: "收货地址",
                            value:
                                "${controller.orderDetail["provinceName"]}${controller.orderDetail["cityName"]}${controller.orderDetail["countiesName"]}${controller.orderDetail["address"]}"),
                        // 收起信息
                        // InkWell(
                        //   onTap: () {},
                        //   child: Row(
                        //     mainAxisAlignment: MainAxisAlignment.center,
                        //     children: [
                        //       Text("收起", style: const TextStyle(color: ColorUtil.black26, fontSize: 16.0),),
                        //       const Icon(Icons.keyboard_arrow_up_outlined, size: 20.0, color: ColorUtil.black26,)
                        //     ],
                        //   ),
                        // ),
                      ],
                    ),
                  ),
                  const SizedBox(
                    height: 10,
                  ),
                  const Divider(height: 1.0, color: ColorUtil.backgroundColor),
                  const SizedBox(
                    height: 10,
                  ),
                  // 快速解决问题
                  Container(
                      padding: const EdgeInsets.symmetric(vertical: 10.0),
                      decoration: const BoxDecoration(
                          color: ColorUtil.white,
                          borderRadius: BorderRadius.all(Radius.circular(8.0))),
                      child: Center(
                        child: InkWell(
                            onTap: () {
                              Get.toNamed(AppPages.contactPage);
                            },
                            splashColor: ColorUtil.grey[200],
                            child: JhText.cText(
                              '联系客服',
                              ColorUtil.black54, 20.0, fontWeight: FontWeight.w500,
                            )),
                      )),
                ],
              ),
            ),
          ),
        ),
        Container(
          padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 10.0),
          decoration: const BoxDecoration(
            color: ColorUtil.white,
          ),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            children: [
              if (controller.orderDetail["status"] == "0")
                TextButton(
                  onPressed: controller.onCancel,
                  style: TextButton.styleFrom(
                      backgroundColor: ColorUtil.white,
                      padding: const EdgeInsets.symmetric(
                          horizontal: 20.0, vertical: 8.0),
                      shape: RoundedRectangleBorder(
                          side: const BorderSide(
                              color: ColorUtil.primaryColor, width: 1.0),
                          borderRadius: BorderRadius.circular(40.0))),
                  child: JhText.cText("取消订单",
                      ColorUtil.primaryColor, 18.0),
                ),
              // const SizedBox(width: 40.0),
              if (controller.orderDetail["status"] == "0")
                TextButton(
                  onPressed: controller.onPay,
                  style: TextButton.styleFrom(
                      backgroundColor: ColorUtil.primaryColor,
                      padding: const EdgeInsets.symmetric(
                          horizontal: 20.0, vertical: 8.0),
                      shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(40.0))),
                  child: JhText.cText("立即支付",
                      ColorUtil.white, 18.0),
                ),
              if (controller.orderDetail["status"] == '2')
                TextButton(
                  onPressed: controller.onComplete,
                  style: TextButton.styleFrom(
                      backgroundColor: ColorUtil.primaryColor,
                      padding: const EdgeInsets.symmetric(
                          horizontal: 20.0, vertical: 8.0),
                      shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(40.0))),
                  child: JhText.cText("确认收货",
                      ColorUtil.white, 18.0),
                ),
              // if (["1", "2", "3"].contains(controller.orderDetail["status"]))
              //   TextButton(
              //     onPressed: controller.onComplete,
              //     style: TextButton.styleFrom(
              //         backgroundColor: ColorUtil.primaryColor,
              //         padding: const EdgeInsets.symmetric(
              //             horizontal: 20.0, vertical: 8.0),
              //         shape: RoundedRectangleBorder(
              //             borderRadius: BorderRadius.circular(40.0))),
              //     child: const Text("确认收货",
              //         style: TextStyle(fontSize: 18.0, color: ColorUtil.white)),
              //   ),
            ],
          ),
        ),
      ],
    );
  }

  Widget orders(String index) {
    return [
      _buildOrderStatus(Icons.access_time, "待付款", "您的订单还未支付"),
      _buildOrderStatus(Icons.access_time, "已支付，待发货", "您的订单已完成支付，等待发货中"),
      _buildOrderStatus(Icons.access_time, "已发货", "您的订单正在运输途中"),
      _buildOrderStatus(Icons.access_time, "已收货", "您的订单已签收"),
      _buildOrderStatus(Icons.access_time, "已完成", "您的订单已完成"),
      _buildOrderStatus(Icons.access_time, "售后", ""),
      _buildOrderStatus(Icons.access_time, "已取消", "您的订单已关闭"),
    ][NumUtil.getIntByValueStr(index) ?? 0];
  }

  Widget _buildOrderStatus(IconData icon, String title, String subtitle) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 30.0),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(icon, size: 30.0, color: ColorUtil.red),
              Text(title,
                  style: const TextStyle(fontSize: 20.0, color: ColorUtil.red))
            ],
          ),
          const SizedBox(height: 10.0),
          JhText.cText(subtitle,
              ColorUtil.black, 15.0),
        ],
      ),
    );
  }
}
