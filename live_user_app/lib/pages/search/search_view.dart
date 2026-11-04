import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:live_app/utils/color_util.dart';
import 'package:live_app/widgets/jh_text.dart';

import '../../routes/app_pages.dart';
import '../../widgets/JhTextField.dart';
import '../../widgets/cache_image.dart';
import '../../widgets/refresh_list.dart';
import 'search_logic.dart';

class SearchPage extends BaseStatefulWidget<SearchLogic> {
  const SearchPage({super.key});

  @override
  bool showAppBar() {
    // TODO: implement showAppBar
    return false;
  }

  @override
  Widget buildContent(BuildContext context) {
    return SafeArea(
      child: Column(
        children: [
          // 搜索栏区域
          _buildSearchBar(),
          // 搜索记录区域
          _buildHistorySection(),
          Expanded(
            child: CustomListView(
              controller: controller.listViewController,
              initialRefresh: false,
              total: controller.goodsList.length,
              itemBuilder: (BuildContext context, int index) {
                return InkWell(
                  child: itemView(index),
                  onTap: (){
                    Get.toNamed(AppPages.goodsDetailPage,
                        arguments: {'id':controller.goodsList[index].goodsId});
                  },
                );
              },
              onRefresh: ()async{
                controller.current.value =1;
                controller.handleRefresh();
              },
              onLoad: ()async{
                controller.current.value++;
                controller.handleRefresh();
              },
            ),
          )
        ],
      ),
    );
  }

  Widget _buildSearchBar() {
    return Container(
      padding: EdgeInsets.symmetric(
        horizontal: 10.w,
      ),
      child: Row(
        children: [
          Expanded(
            child: Container(
              padding: EdgeInsets.only(left: 10.w),
              decoration: BoxDecoration(color: ColorUtil.searchBg, borderRadius: BorderRadius.circular(16.r)),
              alignment: Alignment.center,
              height: 32.w,
              child: JhTextField(
                controller: controller.searchController,
                contentPadding: EdgeInsets.zero,
                hintText: '您喜欢的商品:木梳子/大米/油',
                hintColor: ColorUtil.textGrey,
                hintTextSize: 12,
                textSize: 12,
                leftWidget: Image.asset(
                  'assets/images/ic_search.png',
                  width: 14.w,
                  height: 14.w,
                ),
                textInputAction: TextInputAction.search,
                inputSubmitted: (text) {
                  controller.handleRefresh();
                },
              ),
            ),
          ),
          TextButton(
            onPressed: () {
              Get.back();
            },
            child: JhText.cText(
              '取消',
              ColorUtil.black,
              16,
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildHistorySection() {
    return Container(
      padding: EdgeInsets.fromLTRB(10.w,0,10.w,10.w),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              JhText.cText(
                '搜索记录',
                ColorUtil.black,
                16,
                fontWeight: FontWeight.w600,
              ),
              TextButton(
                onPressed: controller.searchHistory.isEmpty ? null : _clearHistory,
                child: JhText.cText(
                  '全部清空',
                  ColorUtil.textGrey,
                  14,
                ),
              ),
            ],
          ),
          Wrap(
            spacing: 10,
            runSpacing: 10,
            children: controller.searchHistory
                .map((item) => InkWell(
                      child: Container(
                        padding: EdgeInsets.symmetric(horizontal: 13.w, vertical: 5.w),
                        decoration: BoxDecoration(
                          color: ColorUtil.grey100,
                          borderRadius: BorderRadius.circular(16),
                        ),
                        child: JhText.cText(
                          item,
                          Color(0xFFAEAEAE),
                          12,
                        ),
                      ),
                      onTap: () {
                        controller.searchController.text = item;
                        controller.handleRefresh();
                      },
                    ))
                .toList(),
          ),
        ],
      ),
    );
  }

  void _clearHistory() {
    showDialog(
      context: Get.context!,
      builder: (context) => AlertDialog(
        title: JhText.cText(
          '确定清空所有搜索记录？',
          ColorUtil.black,
          18,
        ),
        actions: [
          TextButton(
            style: ButtonStyle(),
            onPressed: () => Get.back(),
            child: JhText.cText(
              '取消',
              ColorUtil.grey,
              14,
            ),
          ),
          TextButton(
            onPressed: () {
              controller.cleanHistory();
            },
            child: JhText.cText('确定', ColorUtil.primaryColor, 14, fontWeight: FontWeight.w600),
          ),
        ],
      ),
    );
  }

  itemView(index) {
    return Card(
      margin: EdgeInsets.only(bottom: 10.w, left: 10.w, right: 10.w),
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(10),
      ),
      child: Padding(
        padding: EdgeInsets.only(right: 10.w),
        child: Row(
          children: [
            CacheImage(
              controller.goodsList[index].goodsImg ?? '',
              width: 120.w,
              height: 120.w,
              radius: 6.r,
            ),
            SizedBox(width: 20.w,),
            Expanded(
                child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                JhText.cText(
                  controller.goodsList[index].goodsTitle ?? '',
                  ColorUtil.black,
                  12,
                ),
                SizedBox(
                  height: 5.w,
                ),
                JhText.cText('￥${controller.goodsList[index].goodsPrice ?? ''}', ColorUtil.priceRed, 16, fontWeight: FontWeight.bold),
              ],
            ))
          ],
        ),
      ),
    );
  }
}
