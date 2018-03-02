package org.caizs.project.detail.service;

import com.baomidou.mybatisplus.entity.TableInfo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.TableInfoHelper;
import org.caizs.project.common.utils.PageUtil;
import org.caizs.project.module.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务基类，封装了基本的方法
 *
 * @param <M> mybatis mapper
 * @param <T> entity
 * @author bjf
 * @desc 方法默认没有使用@Transactional标记事务, 如要使用事务，建议在自己的service里面控制 注意：父类ServiceImpl里面的方法,默认加上@Transactional
 * 如想更灵活，可使用暴露出来的getMapper，自己操作 使用建议：
 * <p>
 * 1.优先使用BaseService里面封装的基本方法，可保证项目里面命名的一致性
 * <p>
 * 2.BaseService里面的方法没有（如：批量操作），则使用ServiceImpl里面的方法（基本都有）
 * <p>
 * 3.使用getMapper()取出Mapper对象，调用自定义方法
 * @date 2017/11/27
 */
@Slf4j
public class BaseService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    private static final int IN_SIZE = 1000;

    /**
     * 新增
     */
    public boolean add(T obj) {
        Integer affCnt = baseMapper.insert(obj);
        return null != affCnt && affCnt > 0;
    }

    /**
     * 修改
     */
    @Override
    public boolean updateById(T obj) {
        Integer affCnt = baseMapper.updateById(obj);
        return null != affCnt && affCnt > 0;
    }

    /**
     * 删除
     */
    public boolean delete(Serializable id) {
        Integer affCnt = baseMapper.deleteById(id);
        return null != affCnt && affCnt > 0;
    }

    /**
     * ID 取对象,取不到为空
     */
    public T get(Serializable id) {
        return baseMapper.selectById(id);
    }

    /**
     * 查询一条记录
     */
    public T getOne(Map<String, String> params) {

        Page page = new Page(1, 2);
        // 不发起count查询
        page.setSearchCount(Boolean.FALSE);

        List<T> list = baseMapper.selectPage(page, this.buildQuery(params));
        if (list.isEmpty()) {
            return null;
        }
        int size = list.size();
        if (size > 1) {
            log.warn("getOne查询到多条记录,只返回第一条");
        }
        return list.get(0);
    }

    /**
     * ID 取对象,取不到对象抛异常
     */
    public T getRequired(Serializable id) {
        T obj = baseMapper.selectById(id);
        if (obj == null) {
            throw new RuntimeException("对象不存在");
        }
        return obj;
    }

    /**
     * count查询
     */
    public int count(Map<String, String> params) {
        return baseMapper.selectCount(this.buildQuery(params));
    }

    /**
     * 只返回分页列表（不发起count查询）
     */
    public List<T> list(Map<String, String> params) {
        Page page = PageUtil.getPage(params);
        // 不发起count查询
        page.setSearchCount(Boolean.FALSE);

        return baseMapper.selectPage(page, this.buildQuery(params));
    }

    /**
     * 返回分页对象
     */
    public PageVO<T> listPage(Map<String, String> params) {
        Page page = PageUtil.getPage(params);
        List<T> list = baseMapper.selectPage(page, this.buildQuery(params));

        return new PageVO(list, page.getTotal());
    }

    /**
     * 查询所有
     */
    public List<T> listAll(Map<String, String> params) {
        return baseMapper.selectList(this.buildQuery(params));
    }

    /**
     * id 集合查询(返回List)
     */
    public List<T> listByIds(List<Serializable> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            log.warn("ids集合为空");
            return Collections.EMPTY_LIST;
        }
        if (ids.size() > IN_SIZE) {
            log.warn("ids集合大于" + IN_SIZE);
            return Collections.EMPTY_LIST;
        }
        return baseMapper.selectBatchIds(ids);
    }

    /**
     * id 集合查询(返回Map)
     *
     * @param clz 主键类型
     */
    public <ID> Map<ID, T> mapByIds(List<Serializable> ids, Class<ID> clz) {
        List<T> list = this.listByIds(ids);
        Map<ID, T> objMap = new HashMap<>(list.size());

        if (list.isEmpty()) {
            return objMap;
        }
        //=== 1.根据对象类型取主键字段
        TableInfo tableInfo = TableInfoHelper.getTableInfo(list.get(0).getClass());
        String idField = tableInfo.getKeyColumn();
        //=== 2.放到map里面减少遍历
        for (T obj : list) {
            try {
                ID idVal;
                String idValStr = BeanUtils.getProperty(obj, idField);
                if (Long.class.isAssignableFrom(clz)) {
                    idVal = (ID) Long.valueOf(idValStr);
                } else if (Integer.class.isAssignableFrom(clz)) {
                    idVal = (ID) Integer.valueOf(idValStr);
                } else {
                    idVal = (ID) idValStr;
                }
                objMap.put(idVal, obj);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return objMap;
    }

    /**
     * 暴露mapper
     */
    public M getMapper() {
        return this.baseMapper;
    }

    protected EntityWrapper<T> buildQuery(Map<String, String> params) {
        return null;
    }


}
