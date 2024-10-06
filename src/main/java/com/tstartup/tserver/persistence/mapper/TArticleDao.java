package com.tstartup.tserver.persistence.mapper;

import com.tstartup.tserver.persistence.dataobject.TArticle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName TArticleDao
 * @Description
 * @Author zhang
 * @Date 2024/8/18 17:16
 * @Version 1.0
 */
@Mapper
@Repository
public interface TArticleDao {


    @Select({
    """
        <script>
                <if test='dto.type!=null and dto.type!=2'>
                    
                </if>
                <foreach collection='dto.uidList' item='uid' open=' AND uid IN (' separator=',' close=')'>
                #{uid}
               </foreach>
            GROUP BY
                LIMIT #{dto.offset},
                #{dto.size};
            SELECT
                FOUND_ROWS() AS total;
        </script>
    """
    })
    List<TArticle> queryArticleList(@Param("tripTypeId") Integer tripTypeId, @Param("cityId") Integer cityId, @Param("start") Integer start, @Param("limit") Integer limit );

}
