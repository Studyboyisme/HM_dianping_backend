package com.hmdp.service.impl;

import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryTypeList() {
        //1.查询redis中是否有店铺类型的缓存
        //获取list集合中的所有元素（JSON字符串）
        List<String> lists = stringRedisTemplate.opsForList().range("cache:shopType", 0, -1);
        List<ShopType> typeList = new ArrayList<>(); //用来存ShopType对象
        //2.判断缓存是否命中
        if(!lists.isEmpty()){
            //3.命中，直接返回（将JSON字符串转换为实体对象后加入到typeList中返回）
            for(String list: lists){
                ShopType shopType = JSONUtil.toBean(list, ShopType.class);
                typeList.add(shopType);
            }
            return Result.ok(typeList);
        }
        //4.缓存没有命中，数据库查询list
        //得到所有shopType的集合
        List<ShopType> shopTypeList = query().orderByAsc("sort").list();
        //不存在元素，报错
        if(shopTypeList.isEmpty()){
            return Result.fail("不存在分类");
        }
        //5.存在，将其写入redis
        //先将对象转换成JSON字符串
        for(ShopType shopType: shopTypeList){
            String jsonStr = JSONUtil.toJsonStr(shopType);
            lists.add(jsonStr);
        }
        //把lists集合写入到redis中
        stringRedisTemplate.opsForList().rightPushAll("cache:shopType", lists);
        //返回信息
        return Result.ok(shopTypeList);
    }
}
