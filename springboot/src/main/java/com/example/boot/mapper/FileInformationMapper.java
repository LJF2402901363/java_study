package com.example.boot.mapper;

import com.example.boot.entity.FileInformation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface FileInformationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FileInformation record);

    int insertSelective(FileInformation record);

    FileInformation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FileInformation record);

    int updateByPrimaryKey(FileInformation record);
  /**
   * @date: 2021/2/18 0018 21:14
   * @description: 查询所有文件信息
   * @param null: 
   * @return: 
   */
    List<FileInformation> findAllFile();
}