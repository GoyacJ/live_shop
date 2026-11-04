import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:live_app/routes/app_pages.dart';
import 'package:live_app/utils/color_util.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';

import '../../../base/widget/base_stateful_widget.dart';
import '../../../models/user_address.dart';
import 'address_list_logic.dart';

class AddressListPage extends BaseStatefulWidget<AddressListLogic> {
  const AddressListPage({super.key});

  @override
  String? title() {
    // TODO: implement title
    return '我的收货地址';
  }

  @override
  List<Widget>? appBarActionWidget() {
    // TODO: implement appBarActionWidget
    return [
      Container(
        alignment: Alignment.center,
        padding: const EdgeInsets.only(right: 16.0),
        child: InkWell(
          onTap: () {
            Get.toNamed(AppPages.addressFormPage)?.then((result) {
              print("返回值：$result");
              if (result != null) {
                controller.onRefresh();
              }
            });
          },
          splashColor: ColorUtil.transparent,
          highlightColor: ColorUtil.transparent,
          hoverColor: ColorUtil.transparent,
          focusColor: ColorUtil.transparent,
          child: Text("+新增地址",
              style: TextStyle(
                  fontSize: 18.sp,
                  color: ColorUtil.primaryColor,
                  fontWeight: FontWeight.bold)),
        ),
      )
    ];
  }

  @override
  Widget buildContent(BuildContext context) {
    return SafeArea(
        child: SmartRefresher(
      enablePullDown: true,
      enablePullUp: false,
      header: const WaterDropHeader(
        refresh: SizedBox(
          width: 20.0,
          height: 20.0,
          child:  CircularProgressIndicator(color: ColorUtil.primaryColor,strokeWidth: 2.0),
        ),
        complete: Center(child: Text("刷新成功")),
      ),
      footer: CustomFooter(
        builder: (context, mode) {
          Widget body;
          if (mode == LoadStatus.idle) {
            body = const Text("上拉加载");
          } else if (mode == LoadStatus.loading) {
            body = const CupertinoActivityIndicator();
          } else if (mode == LoadStatus.failed) {
            body = const Text("加载失败！点击重试！");
          } else if (mode == LoadStatus.canLoading) {
            body = const Text("松手,加载更多!");
          } else {
            body = const Text("没有更多数据了!");
          }
          return SizedBox(
            height: 55.0,
            child: Center(child: body),
          );
        },
      ),
      controller: controller.refreshController,
      onRefresh: controller.onRefresh,
      child: _buildView(),
    ));
  }

  // 主视图
  Widget _buildView() {
    return ListView.builder(
        padding: EdgeInsets.symmetric(horizontal: 10.w, vertical: 15.w),
        itemCount: controller.list.isEmpty ? 1 : controller.list.length,
        itemBuilder: (context, index) {
          if (controller.list.isEmpty) {
            return _buildNull();
          }

          UserAddressEntity item = controller.list[index];
          return InkWell(
            onTap: () {
              if (item.id.isNotEmpty) {
                Get.back(result: item);
              }
            },
            child: Container(
              margin: EdgeInsets.only(bottom: 10.w),
              padding: EdgeInsets.symmetric(vertical: 10.w, horizontal: 12.0),
              decoration: const BoxDecoration(
                  color: ColorUtil.white,
                  borderRadius: BorderRadius.all(Radius.circular(6.0))),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text("${item.nickname}  ${item.mobile}",
                      style: TextStyle(
                          fontSize: 16.sp,
                          color: ColorUtil.black,
                          fontWeight: FontWeight.bold)),
                  const SizedBox(height: 8.0),
                  Text(
                      "${item.provinceName} ${item.cityName} ${item.countiesName} ${item.address}",
                      style: TextStyle(fontSize: 16.sp, color: ColorUtil.black)),
                  const SizedBox(height: 16.0),
                  Row(
                    children: [
                      InkWell(
                        onTap: () => controller.onSelectDefault(index),
                        child: item.isDefault == "1"
                            ? const Row(
                                children: [
                                  Icon(Icons.check_circle,
                                      size: 20.0, color: ColorUtil.primaryColor),
                                  Text("已设为默认",
                                      style: TextStyle(
                                          fontSize: 16.0,
                                          color: ColorUtil.primaryColor))
                                ],
                              )
                            : const Row(
                                children: [
                                  Icon(Icons.circle_outlined,
                                      size: 20.0, color: ColorUtil.black26),
                                  Text("设为默认",
                                      style: TextStyle(
                                          fontSize: 16.0,
                                          color: ColorUtil.black45)),
                                ],
                              ),
                      ),
                      const Expanded(child: SizedBox()),
                      InkWell(
                        onTap: () => controller.onDeleteAddress(index),
                        child: Container(
                          padding: const EdgeInsets.symmetric(
                              horizontal: 8.0, vertical: 2.0),
                          decoration: const BoxDecoration(
                              color: ColorUtil.backgroundColor,
                              borderRadius:
                                  BorderRadius.all(Radius.circular(4.0))),
                          child: Text("删除",
                              style: TextStyle(
                                  fontSize: 15.sp, color: ColorUtil.black54)),
                        ),
                      ),
                      const SizedBox(width: 10.0),
                      InkWell(
                        onTap: () {
                          Get.toNamed(AppPages.addressEditPage, arguments: index)
                              ?.then((result) {
                            print("返回值：$result");
                            if (result != null) {
                              controller.onRefresh();
                            }
                          });
                        },
                        child: Container(
                          padding: const EdgeInsets.symmetric(
                              horizontal: 8.0, vertical: 2.0),
                          decoration: const BoxDecoration(
                              color: ColorUtil.backgroundColor,
                              borderRadius:
                                  BorderRadius.all(Radius.circular(4.0))),
                          child: Text("修改",
                              style: TextStyle(
                                  fontSize: 15.sp, color: ColorUtil.black)),
                        ),
                      )
                    ],
                  ),
                ],
              ),
            ),
          );
        });
  }

  // 暂无记录
  Widget _buildNull() {
    return SizedBox(
      width: double.infinity,
      height: 300.w,
      child: Column(
        mainAxisAlignment: MainAxisAlignment.end,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          Image.asset("assets/images/empty.png",
              height: 100.0, fit: BoxFit.contain),
          const Text("暂无记录",
              style: TextStyle(fontSize: 16.0, color: ColorUtil.black38))
        ],
      ),
    );
  }
}
