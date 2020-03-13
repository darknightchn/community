package self.liuhy.common.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import self.liuhy.common.model.User;

/**
 * @Description: TODO
 * @Author: liuhy
 * @Date 2020/3/9 0:17
 */
@Mapper
public interface UserMapper {


    void create();

    @Insert("insert into user (name, account_id, token, gmt_create, gmt_modified) values(#{name}, #{accountId}, #{token}, #{gmtCreate}, #{gmtModified})")
    void insert(User user);

}
