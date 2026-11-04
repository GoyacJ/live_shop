/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.shop.modular.mallorder.param;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商城-订单导入订单物流编号参数
 *
 **/
@Data
@ExcelIgnoreUnannotated
public class MallOrderLgsnImportParam {

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号", position = 1)
    @ExcelProperty(value = "订单编号", index = 0)
    private String orderNumber;

    /** 物流编号 */
    @ApiModelProperty(value = "物流编号", position = 2)
    @ExcelProperty(value = "物流编号", index = 1)
    private String logisticsNumber;
}
