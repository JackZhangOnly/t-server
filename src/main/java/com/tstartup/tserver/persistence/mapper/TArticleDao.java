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
                SELECT
                    article.*
                FROM
                    t_article article
                    LEFT JOIN t_article_type_relation articleRelation ON article.id = articleRelation.article_id 
                    WHERE article.status = 4
                    AND article.is_hot = #{isHot}
                    <if test='tripTypeId !=null'>
                        AND articleRelation.type_identity = 'tripType'
                        AND articleRelation.type_id  = #{tripTypeId}
                    </if>
                    <if test='cityIdList !=null'>
                        AND articleRelation.type_identity = 'city'                        
                        <foreach collection='cityIdList' item='typeId' open=' AND articleRelation.type_id IN (' separator=',' close=')'>
                          #{typeId}
                       </foreach>
                    </if>
                    order by article.id desc
        </script>
    """
    })
    List<TArticle> queryArticleList(@Param("tripTypeId") Integer tripTypeId, @Param("cityIdList") List<Integer> cityIdList, @Param("isHot") Integer isHot, @Param("start") Integer start, @Param("limit") Integer limit );

}
