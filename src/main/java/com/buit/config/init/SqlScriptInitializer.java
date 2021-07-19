package com.buit.config.init;

import com.buit.config.datasource.DynamicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author yueyu
 * @Date 2020/12/9 13:31
 */
@Configuration
public class SqlScriptInitializer {

    static final Logger log = LoggerFactory.getLogger(SqlScriptInitializer.class);

    @Value("${script.path:script/db/**/*.sql}")
    String scriptPath;
    @Value("${script.service:not set}")
    String serviceName;
    @Autowired
    DataSource dataSource;

    @DynamicDataSource
    @PostConstruct
    public void init() throws SQLException, IOException {
        createChangeLogTable(dataSource.getConnection());
        databasePopulator(dataSource.getConnection());
    }


    private void databasePopulator(Connection con) throws IOException, SQLException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] sqlScripts=null;
        try {
            sqlScripts = resolver.getResources(scriptPath);
        }catch (FileNotFoundException e){
            log.warn("脚本目录为空[{}]",scriptPath);
        }
        if(sqlScripts!=null&&sqlScripts.length!=0){
            List<Resource> resources = Arrays.stream(sqlScripts).collect(Collectors.toList());
            Optional<Resource> notExists = resources.stream().filter(r->!r.exists()).findFirst();
            if(notExists.isPresent()){
                throw new FileNotFoundException(String.format("脚本[%s]不存在",notExists.get().getFilename()));
            }
            String fileNameParam = resources.stream().map(r->String.format("'%s'",r.getFilename())).reduce((s,v)->s.concat(",").concat(v)).get();
            String sql = "select script_name from change_log where script_name in ("+fileNameParam+")";
            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            List<String> executedScript = new ArrayList<>();
            while(rs.next()){
                executedScript.add(rs.getString("script_name"));
            }
            rs.close();
            statement.close();
            ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
            resources.removeIf(s->executedScript.contains(s.getFilename()));
            con.setAutoCommit(false);
            resources.sort(Comparator.comparing((r)->r.getFilename()));
            for (Resource r : resources) {
                try {
                    log.info("执行脚本[{}]",r.getFilename());
                    databasePopulator.setScripts(r);
                    databasePopulator.populate(con);
                    insertChangeLog(con,r.getFilename());
                    con.commit();
                }catch (Exception e){
                    log.error("执行脚本[{}]出错",r.getFilename());
                    con.rollback();
                    throw e;
                }
            }
        }
    }

    private void createChangeLogTable(Connection con) throws SQLException {
        Statement statement = con.createStatement();
        StringBuffer sql = new StringBuffer();
        sql.append("create table if not exists `change_log` (");
        sql.append("`script_name` varchar(255) NOT NULL COMMENT '脚本名称',");
        sql.append("`execute_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '执行时间',");
        sql.append("`service_name` varchar(255) DEFAULT NULL COMMENT '服务名',");
        sql.append(" PRIMARY KEY (`script_name`)");
        sql.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='数据库变更脚本执行表';");
        statement.execute(sql.toString());
        statement.close();
    }

    private void insertChangeLog(Connection con,String fileName) throws SQLException {
        String sql = "insert into change_log(script_name,service_name,execute_time) values(?,?,now())";
        PreparedStatement statement = con.prepareStatement(sql);
        statement.setString(1,fileName);
        statement.setString(2,serviceName);
        statement.execute();
        statement.close();
    }
}
