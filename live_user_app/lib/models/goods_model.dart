
/// Created by Jcy
/// on 2025/5/13
/// 
class GoodsModel {
  String? goodsId;
  String? goodsImg;
  double? goodsPrice;
  double? costPrice;
  String? goodsTitle;

  GoodsModel({this.goodsImg, this.goodsPrice, this.goodsTitle});

  GoodsModel.fromJson(Map<String, dynamic> json) {
    goodsId = json['goodsId'];
    goodsImg = json['goodsImg'];
    costPrice = json['costPrice'];
    goodsPrice = json['goodsPrice'];
    goodsTitle = json['goodsTitle'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data =  {};
    data['goodsId'] = goodsId;
    data['goodsImg'] = goodsImg;
    data['goodsPrice'] = goodsPrice;
    data['costPrice'] = costPrice;
    data['goodsTitle'] = goodsTitle;
    return data;
  }
}
