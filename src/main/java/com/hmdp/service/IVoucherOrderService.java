package com.hmdp.service;

import com.hmdp.dto.Result;
import com.hmdp.entity.VoucherOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
public interface IVoucherOrderService extends IService<VoucherOrder> {

    //实现秒杀下单
    Result seckillVoucher(Long voucherId);

//    Result createVoucherOrder(Long voucherId);
    void createVoucherOrder(VoucherOrder voucherOrder);
}
