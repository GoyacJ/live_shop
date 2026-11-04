import 'package:common_utils/common_utils.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';

import '../../../../models/order_list.dart';
import '../../../../network/net_util.dart';
import '../../../../routes/app_pages.dart';
import '../../../../utils/color_util.dart';
import '../../../../utils/config_util.dart';
import '../../../../widgets/cache_image.dart';

class OrderChildPage extends StatefulWidget {
  final int statusType;
  const OrderChildPage(this.statusType,{super.key});

  @override
  State<OrderChildPage> createState() => _OrderChildPageState();
}

class _OrderChildPageState extends State<OrderChildPage> with AutomaticKeepAliveClientMixin{

  int _page = 1;
  int _total = -1;
  int statusType = -1;
  List<OrderListEntity> list = [];
  RefreshController refreshController = RefreshController(initialRefresh: true);
  List<String> statusList = [
    '待付款',
    '待发货',
    '待收货',
    '已签收',
    '已完成',
    '售后',
    '已取消'
  ];

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    statusType = widget.statusType;
    // _initData();
  }

  _initData() async {
    if (_total>-1 && _page>_total) return;
    final Map<String, dynamic> result = await NetUtil.getOrderList(statusType);
    if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
      list = result['data'].map<OrderListEntity>((item) {
        return OrderListEntity.fromJson(item);
      }).toList();
    }
    print("长度：${list.length}");
    setState(() {});
  }

  Future onRefresh() async{
    print("下拉刷新");
    _page = 1;
    await _initData();
    // if failed,use refreshFailed()
    refreshController.refreshCompleted();
  }

  Future onLoading() async{
    print("上拉加载");
    await _initData();
    refreshController.loadComplete();
  }
  
  @override
  Widget build(BuildContext context) {
    super.build(context);
    return SafeArea(
        child: SmartRefresher(
          enablePullDown: true,
          // enablePullUp: list.isEmpty ? false : true,
          enablePullUp: false,
          header:  const WaterDropHeader(
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
          controller: refreshController,
          onRefresh: onRefresh,
          // onLoading: onLoading,
          child: _buildView(),
        ));
  }

  // 主视图
  Widget _buildView() {
    return ObjectUtil.isEmptyList(list)
        ? _buildNull()
        : ListView.builder(
        itemCount: list.length,
        itemBuilder: (context, index) {
          return InkWell(
            onTap: () {
              Get.toNamed(AppPages.orderDetailPage,
                  arguments: list[index].id)
                  ?.then((result) {
                print("回传：$result");
                if (result != null) {
                  print("开始刷新");
                  onRefresh();
                }
              });
            },
            child: _buildOrderItem(list[index]),
          );
        });
  }

  // 订单
  Widget _buildOrderItem(OrderListEntity item) {
    return Container(
      margin: EdgeInsets.symmetric(horizontal: 10.w, vertical: 5.w),
      padding: EdgeInsets.symmetric(horizontal: 8.w, vertical: 6.w),
      decoration: const BoxDecoration(
          color: ColorUtil.white,
          borderRadius: BorderRadius.all(Radius.circular(10.0)),
          boxShadow: [
            BoxShadow(
              color: ColorUtil.black12,
              offset: Offset(0.0, 0.0), //阴影xy轴偏移量
              blurRadius: 2.0, //阴影模糊程度
              spreadRadius: 1.0, //阴影扩散程度
            )
          ]),
      child: Column(children: [
        Row(
          children: [
            Text(item.orderNumber,
                style: const TextStyle(fontSize: 18.0, color: ColorUtil.black)),
            const Spacer(),
            Text(statusList[item.status],
                style: const TextStyle(
                    fontSize: 16.0, color: ColorUtil.primaryColor))
          ],
        ),
        SizedBox(height: 6.w),
        const Divider(height: 1.0, color: ColorUtil.backgroundColor),
        SizedBox(height: 6.w),
        for (var item in item.goods)
          Row(
            children: [
              CacheImage(
                item.goodsCover,
                width: 80.w,
                height: 80.w,
              ),
              SizedBox(width: 6.w),
              Expanded(
                child: SizedBox(
                  height: 80.w,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(item.goodsTitle,
                          style: const TextStyle(
                              fontSize: 16.0, color: ColorUtil.black),
                          maxLines: 3,
                          overflow: TextOverflow.ellipsis),
                    ],
                  ),
                ),
              ),
              Column(
                children: [
                  Text("¥${item.goodsPrice}",
                      style: const TextStyle(
                          fontSize: 16.0, color: ColorUtil.primaryColor)),
                  Text("${item.goodsCount}件",
                      style: const TextStyle(
                          fontSize: 16.0, color: ColorUtil.primaryColor)),
                ],
              )
            ],
          )
      ]),
    );
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

  @override
  void dispose() {
    refreshController.dispose();
    super.dispose();
  }

  @override
  // TODO: implement wantKeepAlive
  bool get wantKeepAlive => true;
}

