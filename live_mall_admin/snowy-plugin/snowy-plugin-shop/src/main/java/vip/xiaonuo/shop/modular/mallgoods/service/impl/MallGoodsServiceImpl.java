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
package vip.xiaonuo.shop.modular.mallgoods.service.impl;

import api.shop.dto.MallGoodsSearchDTO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.row.AbstractRowHeightStyleStrategy;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.Context;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.excel.CommonExcelCustomMergeStrategy;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.common.util.CommonDownloadUtil;
import vip.xiaonuo.common.util.CommonResponseUtil;
import vip.xiaonuo.shop.modular.mallgoods.entity.MallGoods;
import vip.xiaonuo.shop.modular.mallgoods.mapper.MallGoodsMapper;
import vip.xiaonuo.shop.modular.mallgoods.param.*;
import vip.xiaonuo.shop.modular.mallgoods.result.MallGoodsExportResult;
import vip.xiaonuo.shop.modular.mallgoods.service.MallGoodsService;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 商城-商品Service接口实现类
 *
 * @author yuezisheng
 * @date 2025/02/20 15:25
 **/
@Component
@Slf4j
public class MallGoodsServiceImpl extends ServiceImpl<MallGoodsMapper, MallGoods> implements MallGoodsService {

    @Inject
    private MallGoodsMapper mallGoodsMapper;

    @Inject
    private CommonCacheOperator commonCacheOperator;

    @Override
    public Page<MallGoods> page(MallGoodsPageParam mallGoodsPageParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (ObjectUtil.isNotEmpty(mallGoodsPageParam.getTitle())) {
            queryWrapper.like(MallGoods::getTitle, mallGoodsPageParam.getTitle());
        }
        if (ObjectUtil.isNotEmpty(mallGoodsPageParam.getStartCreateTime()) && ObjectUtil.isNotEmpty(mallGoodsPageParam.getEndCreateTime())) {
            queryWrapper.between(MallGoods::getCreateTime, mallGoodsPageParam.getStartCreateTime(), mallGoodsPageParam.getEndCreateTime());
        }

        // 关键词查询
        if (ObjectUtil.isNotEmpty(mallGoodsPageParam.getSearchKey())) {
            // sql注入处理
            queryWrapper.and(wq -> {
                wq.where(wq1 -> {
                    wq1.or(MallGoods::getTitle).like(mallGoodsPageParam.getSearchKey());
                    wq1.or(MallGoods::getDescription).like(mallGoodsPageParam.getSearchKey());
                });
            });
        }

        if (ObjectUtil.isAllNotEmpty(mallGoodsPageParam.getSortField(), mallGoodsPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(mallGoodsPageParam.getSortOrder());
            queryWrapper.orderBy(StrUtil.toUnderlineCase(mallGoodsPageParam.getSortField()), mallGoodsPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(MallGoods::getCreateTime, false);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Tran
    @Override
    public void add(MallGoodsAddParam mallGoodsAddParam) {
        if (ObjectUtil.isEmpty(mallGoodsAddParam)) {
            throw new CommonException("商品信息不能为空");
        }
        try {
            MallGoods mallGoods = BeanUtil.toBean(mallGoodsAddParam, MallGoods.class);
            this.save(mallGoods);
        } catch (Exception e) {
            throw new CommonException("商品信息添加失败");
        }
    }

    @Tran
    @Override
    public void edit(MallGoodsEditParam mallGoodsEditParam) {
        //处理轮播图数据
        String carouseImage = mallGoodsEditParam.getCarouseImage();
        if (StrUtil.equals(carouseImage.substring(carouseImage.length() - 1), ",")) {
            StringBuilder sb = new StringBuilder(carouseImage);
            sb.deleteCharAt(sb.length() - 1);
            mallGoodsEditParam.setCarouseImage(sb.toString());
        }
        //处理主图
        String hostImage = mallGoodsEditParam.getHostImage();
        if (hostImage == null) {
            mallGoodsEditParam.setHostImage("");
        }
        MallGoods mallGoods = this.queryEntity(mallGoodsEditParam.getId());
        if (ObjectUtil.isEmpty(mallGoods)) {
            throw new CommonException("商品信息不存在");
        }
        try {
            BeanUtil.copyProperties(mallGoodsEditParam, mallGoods);
            this.updateById(mallGoods);
        } catch (Exception e) {
            throw new CommonException("商品信息修改失败");
        }
    }

    @Tran
    @Override
    public void delete(List<MallGoodsIdParam> mallGoodsIdParamList) {
        try {
            // 执行删除
            this.removeByIds(CollStreamUtil.toList(mallGoodsIdParamList, MallGoodsIdParam::getId));
        } catch (Exception e) {
            throw new CommonException("商品信息删除失败");
        }
    }

    @Override
    public MallGoods detail(MallGoodsIdParam mallGoodsIdParam) {
        return this.queryEntity(mallGoodsIdParam.getId());
    }

    @Override
    public MallGoods queryEntity(String id) {
        MallGoods mallGoods = this.getById(id);
        if (ObjectUtil.isEmpty(mallGoods)) {
            throw new CommonException("商城-商品不存在，id值为：{}", id);
        }
        return mallGoods;
    }

    @Override
    public List<MallGoods> detailList() {
        QueryWrapper queryWrapper = new QueryWrapper();
        List<MallGoods> list = this.list(queryWrapper);
        return list;
    }

    @Override
    public void exportMallGoods(MallGoodsExportParam mallGoodsExportParam, Context context) throws IOException {
        File tempFile = null;
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            if (ObjectUtil.isNotEmpty(mallGoodsExportParam.getGoodsIds())) {
                queryWrapper.in(MallGoods::getId, StrUtil.split(mallGoodsExportParam.getGoodsIds(), StrUtil.COMMA));
            } else {
                if (ObjectUtil.isNotEmpty(mallGoodsExportParam.getSearchKey())) {
                    queryWrapper.like(MallGoods::getTitle, mallGoodsExportParam.getSearchKey());
                }
                if (ObjectUtil.isNotEmpty(mallGoodsExportParam.getGoodsStatus())) {
                    queryWrapper.eq(MallGoods::getStatus, mallGoodsExportParam.getGoodsStatus());
                }
            }
            String fileName = "商品信息清单.xlsx";
            List<MallGoods> mallGoodsList = this.list(queryWrapper);
            if (ObjectUtil.isEmpty(mallGoodsList)) {
                throw new CommonException("无数据可导出");
            }
//            transService.transBatch(mallGoodsList);

            mallGoodsList = CollectionUtil.sort(mallGoodsList, Comparator.comparing(MallGoods::getId));
            List<MallGoodsExportResult> mallGoodsExportResultList = mallGoodsList.stream()
                    .map(mallGood -> {
                        MallGoodsExportResult mallGoodsExportResult = new MallGoodsExportResult();
                        BeanUtil.copyProperties(mallGood, mallGoodsExportResult);
                        mallGoodsExportResult.setTitle(ObjectUtil.isNotEmpty(mallGoodsExportResult.getTitle()) ?
                                mallGoodsExportResult.getTitle() : "无组织");
                        if (StrUtil.equals(mallGood.getStatus(), "1")) {
                            mallGoodsExportResult.setStatus("正常");
                        } else {
                            mallGoodsExportResult.setStatus("下架");
                        }
                        return mallGoodsExportResult;
                    }).collect(Collectors.toList());
            // 创建临时文件
            tempFile = FileUtil.file(FileUtil.getTmpDir() + FileUtil.FILE_SEPARATOR + fileName);

            // 头的策略
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            WriteFont headWriteFont = new WriteFont();
            headWriteFont.setFontHeightInPoints((short) 14);
            headWriteCellStyle.setWriteFont(headWriteFont);
            // 水平垂直居中
            headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            // 内容的策略
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
            contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
            // 内容背景白色
            contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            WriteFont contentWriteFont = new WriteFont();

            // 内容字体大小
            contentWriteFont.setFontHeightInPoints((short) 12);
            contentWriteCellStyle.setWriteFont(contentWriteFont);

            //设置边框样式，细实线
            contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
            contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
            contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
            contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);

            // 水平垂直居中
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
            contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
            HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle,
                    contentWriteCellStyle);


            // 写excel
            EasyExcel.write(tempFile.getPath(), MallGoodsExportResult.class)
                    // 自定义样式
                    .registerWriteHandler(horizontalCellStyleStrategy)
                    // 自动列宽
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    // 机构分组合并单元格
                    .registerWriteHandler(new CommonExcelCustomMergeStrategy(mallGoodsExportResultList.stream().map(MallGoodsExportResult::getTitle)
                            .collect(Collectors.toList()), 0))
                    // 设置第一行字体
                    .registerWriteHandler(new CellWriteHandler() {
                        @Override
                        public void afterCellDispose(CellWriteHandlerContext context) {
                            WriteCellData<?> cellData = context.getFirstCellData();
                            WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();
                            Integer rowIndex = context.getRowIndex();
                            if (rowIndex == 0) {
                                WriteFont headWriteFont = new WriteFont();
                                headWriteFont.setFontName("宋体");
                                headWriteFont.setBold(true);
                                headWriteFont.setFontHeightInPoints((short) 16);
                                writeCellStyle.setWriteFont(headWriteFont);
                            }
                        }
                    })
                    // 设置表头行高
                    .registerWriteHandler(new AbstractRowHeightStyleStrategy() {
                        @Override
                        protected void setHeadColumnHeight(Row row, int relativeRowIndex) {
                            if (relativeRowIndex == 0) {
                                // 表头第一行
                                row.setHeightInPoints(34);
                            } else {
                                // 表头其他行
                                row.setHeightInPoints(30);
                            }
                        }

                        @Override
                        protected void setContentColumnHeight(Row row, int relativeRowIndex) {
                            // 内容行
                            row.setHeightInPoints(20);
                        }
                    })
                    .sheet("商品信息")
                    .doWrite(mallGoodsExportResultList);
            CommonDownloadUtil.download(tempFile, context);
        } catch (Exception e) {
            CommonResponseUtil.renderError(context, "用户导出失败");
        } finally {
            FileUtil.del(tempFile);
        }
    }

    @Override
    public List<MallGoods> mallGoodsList(MallGoodsIdAndTitleParam mallGoodsIdAndTitleParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (ObjectUtil.isNotEmpty(mallGoodsIdAndTitleParam.getId())) {
            queryWrapper.eq("goods_id", mallGoodsIdAndTitleParam.getId());
        }
        if (ObjectUtil.isNotEmpty(mallGoodsIdAndTitleParam.getTitle())) {
            queryWrapper.like("title", mallGoodsIdAndTitleParam.getTitle());
        }
        List<MallGoods> list = this.list(queryWrapper);
        return list;
    }

    /**
     * 扣减商城商品库存
     * 采用分布式锁+乐观锁方式实现
     *
     * @param goodsId 商品Id
     * @param stock   扣减数量
     * @return 扣减结果
     */
    @Override
    @Tran
    public boolean deductionMallGoodsStock(String goodsId, Integer stock) {
        if (stock <= 0) {
            return true;
        }
        String lockKey = "goods:stock:" + goodsId;

        try {
            boolean locked = commonCacheOperator.tryLock(lockKey);
            if (!locked) {
                return false;
            }
            // 后续库存扣减逻辑
            return mallGoodsMapper.deductionStock(goodsId, stock) > 0;
        } catch (Exception e) {
            log.error("扣减库存异常", e);
            return false;
        } finally {
            commonCacheOperator.unLock(lockKey);
        }
    }

    @Override
    public boolean supplementGoodsStock(String goodsId, Integer stock) {
        if (stock <= 0) {
            return true;
        }
        String lockKey = "goods:stock:" + goodsId;

        try {
            boolean locked = commonCacheOperator.tryLock(lockKey);
            if (!locked) {
                return false;
            }
            // 后续库存扣减逻辑
            return mallGoodsMapper.supplementGoodsStock(goodsId, stock) > 0;
        } catch (Exception e) {
            log.error("回补库存异常", e);
            return false;
        } finally {
            commonCacheOperator.unLock(lockKey);
        }
    }

    @Override
    public Page<MallGoods> search(MallGoodsSearchDTO param) {

        QueryWrapper queryWrapper = new QueryWrapper();
        if (ObjectUtil.isNotEmpty(param.title())) {
            queryWrapper.like(MallGoods::getTitle, param.title());
        }

        if (Objects.nonNull(param.sales())) {
            CommonSortOrderEnum.validate(param.sales().getValue());
            queryWrapper.orderBy(MallGoods::getSales, param.sales().getValue().equals(CommonSortOrderEnum.ASC.getValue()));
        } else if (Objects.nonNull(param.price())) {
            CommonSortOrderEnum.validate(param.price().getValue());
            queryWrapper.orderBy(MallGoods::getPrice, param.price().getValue().equals(CommonSortOrderEnum.ASC.getValue()));
        } else if (Objects.nonNull(param.sort())) {
            CommonSortOrderEnum.validate(param.sort().getValue());
            queryWrapper.orderBy(MallGoods::getSort, param.sort().getValue().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(MallGoods::getCreateTime, false);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }
}
