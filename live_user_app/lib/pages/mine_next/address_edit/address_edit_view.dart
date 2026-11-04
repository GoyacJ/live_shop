import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

import '../../../base/widget/base_stateful_widget.dart';
import '../../../utils/color_util.dart';
import 'address_edit_logic.dart';

class AddressEditPage extends BaseStatefulWidget<AddressEditLogic> {
  const AddressEditPage({super.key});

  @override
  String? title() {
    // TODO: implement title
    return '修改收货地址';
  }

  @override
  Widget buildContent(BuildContext context) {
    return SingleChildScrollView(
      child: _buildView(),
    );
  }

  // 主视图
  Widget _buildView() {
    return Column(
      children: [
        Container(
          margin: const EdgeInsets.only(top: 10.0, left: 10.0, right: 10.0),
          padding: EdgeInsets.all(15.0),
          decoration: BoxDecoration(
            color: ColorUtil.white,
            borderRadius: BorderRadius.circular(6.0),
          ),
          child: Column(
            children: [
              // 收货人
              Row(
                children: [
                  SizedBox(
                    width: 80.w,
                    child: Text("收货人", style: TextStyle(fontSize: 16.sp, color: ColorUtil.black)),
                  ),
                  Expanded(
                    child: TextField(
                      keyboardType: TextInputType.name,
                      controller: controller.nicknameController,
                      style: TextStyle(color: ColorUtil.black, fontSize: 16.sp),
                      cursorColor: ColorUtil.black54,
                      decoration: InputDecoration(
                        contentPadding: const EdgeInsets.symmetric(vertical: 16.0),
                        hintText: '请输入收货人姓名',
                        hintStyle: const TextStyle(color: ColorUtil.black38),
                        enabledBorder: UnderlineInputBorder(
                          borderSide: const BorderSide(
                            color: ColorUtil.backgroundColor,
                            width: 1,
                          ),
                          borderRadius: BorderRadius.circular(0),
                        ),
                        focusedBorder: UnderlineInputBorder(
                          borderSide: const BorderSide(
                            color: ColorUtil.backgroundColor,
                            width: 1,
                          ),
                          borderRadius: BorderRadius.circular(0),
                        ),
                        border: OutlineInputBorder(
                          borderSide: const BorderSide(
                            color: ColorUtil.transparent,
                            width: 0,
                          ),
                          borderRadius: BorderRadius.circular(0),
                        ),
                      ),
                      onChanged: (value) {},
                    ),
                  )
                ],
              ),
              // const SizedBox(height: 10.0),
              // 手机号
              Row(
                children: [
                  SizedBox(
                    width: 80.w,
                    child: Text("手机号", style: TextStyle(fontSize: 16.sp, color: ColorUtil.black)),
                  ),
                  Expanded(
                    child: TextField(
                      keyboardType: TextInputType.phone,
                      controller: controller.mobileController,
                      style: TextStyle(color: ColorUtil.black, fontSize: 16.sp),
                      cursorColor: ColorUtil.black54,
                      decoration: InputDecoration(
                        contentPadding: const EdgeInsets.symmetric(vertical: 16.0),
                        hintText: '请输入手机号',
                        hintStyle: const TextStyle(color: ColorUtil.black38),
                        enabledBorder: UnderlineInputBorder(
                          borderSide: const BorderSide(
                            color: ColorUtil.backgroundColor,
                            width: 1,
                          ),
                          borderRadius: BorderRadius.circular(0),
                        ),
                        focusedBorder: UnderlineInputBorder(
                          borderSide: const BorderSide(
                            color: ColorUtil.backgroundColor,
                            width: 1,
                          ),
                          borderRadius: BorderRadius.circular(0),
                        ),
                        border: OutlineInputBorder(
                          borderSide: const BorderSide(
                            color: ColorUtil.transparent,
                            width: 0,
                          ),
                          borderRadius: BorderRadius.circular(0),
                        ),
                      ),
                      onChanged: (value) {},
                    ),
                  )
                ],
              ),
              // const SizedBox(height: 10.0),
              //所在地区
              Row(
                  children: [
                    SizedBox(
                      width: 80.w,
                      child: Text("所在地区", style: TextStyle(fontSize: 16.sp, color: ColorUtil.black)),
                    ),
                    Expanded(
                      child: Container(
                        padding: const EdgeInsets.symmetric(vertical: 16.0),
                        decoration: const BoxDecoration(
                          // 底部边框
                          border: Border(
                            bottom: BorderSide(
                              color: ColorUtil.backgroundColor,
                              width: 1,
                            ),
                          ),
                        ),
                        child: InkWell(
                            onTap: controller.onSelectAddress,
                            child: Row(
                              children: [
                                Expanded(
                                  child: controller.counties.value.isEmpty
                                      ? Text("请选择省市区", style: TextStyle(color: ColorUtil.black38, fontSize: 16.sp))
                                      : Text("${controller.province.value} ${controller.city.value} ${controller.counties.value}", style: TextStyle(color: ColorUtil.black, fontSize: 16.sp)),
                                ),
                                const SizedBox(width: 10.0),
                                Icon(Icons.keyboard_arrow_right, size: 24.sp, color: ColorUtil.black38),
                              ],
                            )
                        ),
                      ),
                    )
                  ]
              ),
              // 详细地址
              Row(
                children: [
                  SizedBox(
                    width: 80.w,
                    child: Text("详细地址", style: TextStyle(fontSize: 16.sp, color: ColorUtil.black)),
                  ),
                  Expanded(
                    child: TextField(
                      keyboardType: TextInputType.text,
                      controller: controller.addressController,
                      style: TextStyle(color: ColorUtil.black, fontSize: 16.sp),
                      cursorColor: ColorUtil.black54,
                      decoration: InputDecoration(
                        contentPadding: EdgeInsets.symmetric(vertical: 16.0),
                        hintText: '选择省市区街道',
                        hintStyle: const TextStyle(color: ColorUtil.black38),
                        enabledBorder: UnderlineInputBorder(
                          borderSide: const BorderSide(
                            color: ColorUtil.backgroundColor,
                            width: 1,
                          ),
                          borderRadius: BorderRadius.circular(0),
                        ),
                        focusedBorder: UnderlineInputBorder(
                          borderSide: const BorderSide(
                            color: ColorUtil.backgroundColor,
                            width: 1,
                          ),
                          borderRadius: BorderRadius.circular(0),
                        ),
                        border: OutlineInputBorder(
                          borderSide: const BorderSide(
                            color: ColorUtil.transparent,
                            width: 0,
                          ),
                          borderRadius: BorderRadius.circular(0),
                        ),
                      ),
                      onChanged: (value) {},
                    ),
                  )
                ],
              ),
            ],
          ),
        ),
        const SizedBox(height: 80.0),
        InkWell(
          onTap: controller.onSubmit,
          child: Container(
            width: 300.w,
            height: 50.w,
            decoration: BoxDecoration(
                color: ColorUtil.primaryColor,
                borderRadius: BorderRadius.circular(25.w)
            ),
            child: Center(
              child: Text("保存", style: TextStyle(color: ColorUtil.white, fontSize: 18.sp)),
            ),
          ),
        ),
      ],
    );
  }
}