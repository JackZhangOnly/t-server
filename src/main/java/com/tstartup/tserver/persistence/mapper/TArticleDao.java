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


    /**
     *
     <if test='cityIdList !=null'>
     <foreach collection='cityIdList' item='typeId' open=' AND article.dest_city IN (' separator=',' close=')'>
     #{typeId}
     </foreach>
     </if>
     * @param articleTypeId
     * @param tripTypeId
     * @param countryId
     * @param isHot
     * @param start
     * @param limit
     * @return
     */
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
                    <if test='articleTypeId !=null'>
                        AND articleRelation.type_identity = 'articleType'
                        AND articleRelation.type_id  = #{articleTypeId}
                    </if>
                    <if test='countryId !=null'>
                         AND article.dest_country = #{countryId}
                    </if>
                    <if test='cityId !=null'>
                         AND article.dest_city = #{cityId}
                    </if>
                    order by article.id desc
                    limit #{start},#{limit}

        </script>
    """
    })
    List<TArticle> queryArticleList(@Param("articleTypeId") Integer articleTypeId,@Param("tripTypeId") Integer tripTypeId, @Param("countryId") Integer countryId, @Param("cityId") Integer cityId, @Param("isHot") Integer isHot, @Param("start") Integer start, @Param("limit") Integer limit);

}
