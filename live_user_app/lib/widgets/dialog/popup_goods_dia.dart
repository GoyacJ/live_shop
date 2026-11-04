import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:live_app/routes/app_pages.dart';
import 'package:get/get.dart';
import '../../utils/color_util.dart';
import '../jh_text.dart';
import '../cache_image.dart';

/// 底部商品框
import '../../models/live_goods.dart';

class PopupGoodsDia extends StatefulWidget {
  const PopupGoodsDia(
      {super.key, this.shopCover, this.shopTitle, required this.goodsList});

  final String? shopCover;
  final String? shopTitle;
  final List<LiveGoodsEntity> goodsList;

  @override
  State<PopupGoodsDia> createState() => _PopupGoodsDiaState();
}

class _PopupGoodsDiaState extends State<PopupGoodsDia> {
  List goodsTag = [
    '全部',
    '零食产品',
    '生鲜',
    '家电',
    '个人护理',
    '粮油调味',
    '生活电器',
    '书籍杂志',
    '粮油调味',
    '生活电器',
    '书籍杂志'
  ];

  @override
  void initState() {
    super.initState();
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    double height = MediaQuery.of(context).size.height;
    return Container(
      height: height-(height/4),
      decoration: BoxDecoration(
          color: ColorUtil.white, borderRadius: BorderRadius.circular(8)),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Container(
            padding: const EdgeInsets.only(
                left: 10.0, top: 10.0, bottom: 10.0, right: 15.0),
            decoration: const BoxDecoration(
                border: Border(bottom: BorderSide(color: ColorUtil.grey50))),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  children: [
                    Expanded(
                      child: Row(
                        children: [
                          ClipOval(
                              child: CacheImage(
                            widget.shopCover!,
                            width: 35.0,
                            height: 35.0,
                          )),
                          const SizedBox(
                            width: 7.0,
                          ),
                          Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Row(
                                children: [
                                  JhText.cText(
                                    widget.shopTitle!,
                                    ColorUtil.black, 16.0,
                                    fontWeight: FontWeight.w600,
                                  ),
                                  const Icon(
                                    Icons.arrow_forward_ios,
                                    size: 12.0,
                                  ),
                                ],
                              ),
                              Row(
                                children: [
                                  JhText.cText('店铺评价',
                                      ColorUtil.red, 12.0),
                                  SizedBox(
                                    width: 7.0,
                                  ),
                                  JhText.cText('5.00 高',
                                      ColorUtil.blue, 12.0),
                                ],
                              ),
                            ],
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ],
            ),
          ),
          Expanded(
            child: widget.goodsList.isEmpty
                ? Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Image.asset("assets/images/empty.png",
                          height: 100.0, fit: BoxFit.contain),
                      JhText.cText("暂无记录",
                          ColorUtil.black38, 16.0)
                    ],
                  )
                : ListView.builder(
                    shrinkWrap: true,
                    itemCount: widget.goodsList.length,
                    itemBuilder: (context, index) {
                      return InkWell(
                        onTap: () {
                          Get.offNamed(AppPages.goodsDetailPage,
                              arguments: {'data':widget.goodsList[index]});
                        },
                        child: Container(
                          padding: const EdgeInsets.symmetric(
                              horizontal: 10.0, vertical: 7.0),
                          child: Row(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Stack(
                                children: [
                                  ClipRRect(
                                    borderRadius: BorderRadius.circular(6.0),
                                    child: Image.network(
                                      widget.goodsList[index].cover,
                                      height: 100.0,
                                      width: 100.0,
                                      fit: BoxFit.cover,
                                    ),
                                  ),
                                  Positioned(
                                    left: 0.0,
                                    top: 0.0,
                                    child: Container(
                                      padding: const EdgeInsets.symmetric(
                                          horizontal: 10.0, vertical: 1.0),
                                      decoration: const BoxDecoration(
                                        color: ColorUtil.black54,
                                        borderRadius: BorderRadius.only(
                                          topLeft: Radius.circular(6.0),
                                          bottomRight: Radius.circular(6.0),
                                        ),
                                      ),
                                      child: JhText.cText(
                                        '${index + 1}',
                                        ColorUtil.white, 12.0,
                                      ),
                                    ),
                                  ),
                                ],
                              ),
                              const SizedBox(
                                width: 10.0,
                              ),
                              Expanded(
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    JhText.cText(
                                      widget.goodsList[index].title,
                                      ColorUtil.black, 16.0,
                                      overflow: TextOverflow.ellipsis,
                                    ),
                                    // Text('${widget.goodsList![index]['tips']}', style: const TextStyle(color: ColorUtil.red, fontSize: 12.0,),),
                                    const SizedBox(
                                      height: 30.0,
                                    ),
                                    Row(
                                      children: [
                                        Expanded(
                                          child: Row(children: [
                                            Row(
                                              children: [
                                                JhText.cText(
                                                  '￥',
                                                  ColorUtil.red, 12.0,
                                                ),
                                                JhText.cText(
                                                  '${widget.goodsList[index].price}',
                                                  ColorUtil.red, 16.0,
                                                ),
                                              ],
                                            ),
                                            Row(
                                              children: [
                                                JhText.cText(
                                                  ' 原价',
                                                  ColorUtil.grey, 10.0,
                                                ),
                                                JhText.cText(
                                                  '￥${widget.goodsList[index].price}',
                                                  ColorUtil.grey, 10.0,
                                                ),
                                              ],
                                            ),
                                          ]),
                                        ),
                                        InkWell(
                                            onTap: () {
                                              Map<String,dynamic>params = {
                                                'id':widget.goodsList[index].id,
                                                'price':widget.goodsList[index].price,
                                                'liveId':widget.goodsList[index].liveId,
                                                'cover':widget.goodsList[index].cover,
                                                'title':widget.goodsList[index].title,
                                              };
                                              Get.offNamed(
                                                  AppPages.createOrderPage, arguments:params);
                                            },
                                            child: Container(
                                              padding:
                                                  const EdgeInsets.symmetric(
                                                      horizontal: 10.0,
                                                      vertical: 5.0),
                                              decoration: BoxDecoration(
                                                color: ColorUtil.red,
                                                borderRadius:
                                                    BorderRadius.circular(6.0),
                                              ),
                                              child:  JhText.cText(
                                                '立即购买',
                                                ColorUtil.white, 18.0,
                                                fontWeight: FontWeight.w500),
                                            )),
                                      ],
                                    ),
                                  ],
                                ),
                              ),
                            ],
                          ),
                        ),
                      );
                    },
                  ),
          ),
        ],
      ),
    );
  }
}
