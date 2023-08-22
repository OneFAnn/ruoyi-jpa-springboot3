package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.system.repository.SysDictDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.utils.DictUtils;

import com.ruoyi.system.service.ISysDictDataService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 字典 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysDictDataServiceImpl implements ISysDictDataService
{

    @Autowired
    private SysDictDataRepository dictDataRepository;

    /**
     * 根据条件分页查询字典数据
     * 
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    @Override
    public List<SysDictData> selectDictDataList(SysDictData dictData, PageDomain pageDomain)
    {
        return dictDataRepository.selectDictDataList(dictData,pageDomain);
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     * 
     * @param dictType 字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    @Override
    public String selectDictLabel(String dictType, String dictValue)
    {
        return dictDataRepository.selectDictLabel(dictType, dictValue);
    }

    /**
     * 根据字典数据ID查询信息
     * 
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    @Override
    public SysDictData selectDictDataById(Long dictCode)
    {
        return dictDataRepository.findById(dictCode).get();
    }

    /**
     * 批量删除字典数据信息
     * 
     * @param dictCodes 需要删除的字典数据ID
     */
    @Override
    public void deleteDictDataByIds(Long[] dictCodes)
    {
        for (Long dictCode : dictCodes)
        {
            SysDictData data = selectDictDataById(dictCode);
            dictDataRepository.delete(data);
            List<SysDictData> dictDatas = dictDataRepository.selectDictDataByType(data.getDictType());
            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
    }

    /**
     * 新增保存字典数据信息
     * 
     * @param data 字典数据信息
     * @return 结果
     */
    @Override
    public int insertDictData(SysDictData data)
    {
        dictDataRepository.save(data);
        List<SysDictData> dictDatas = dictDataRepository.selectDictDataByType(data.getDictType());
        DictUtils.setDictCache(data.getDictType(), dictDatas);
        return 1;
    }

    /**
     * 修改保存字典数据信息
     * 
     * @param data 字典数据信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateDictData(SysDictData data)
    {
        dictDataRepository.updateDictData(data);
        List<SysDictData> dictDatas = dictDataRepository.selectDictDataByType(data.getDictType());
        DictUtils.setDictCache(data.getDictType(), dictDatas);

        return 1;
    }
}
