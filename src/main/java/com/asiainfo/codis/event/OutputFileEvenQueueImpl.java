package com.asiainfo.codis.event;

import codis.Conf;
import com.asiainfo.codis.conf.StatisticalTablesConf;
import com.asiainfo.codis.util.OutputFileUtils;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by peng on 16/9/6.
 */
public class OutputFileEvenQueueImpl extends EventQueue<List<String>>{
    private static Logger logger = Logger.getLogger(OutputFileEvenQueueImpl.class);
    public OutputFileEvenQueueImpl() {
        String hdfsOutputPath = Conf.getProp("hdfs.output.path");
        if (Conf.getBoolean(Conf.EXPORT_FILE_ENABLE, Conf.DEFAULT_EXPORT_FILE_ENABLE)){
            OutputFileUtils.setHdfsOutputPath(hdfsOutputPath);
            OutputFileUtils.init();
        }
    }

    @Override
    public boolean consumeEvent() {
        List<String> event = null;
        long startTime=System.currentTimeMillis();
        logger.debug("Start to export data to local ...");
        try {
            event = events.take();
        } catch (InterruptedException e) {
            logger.error("Unknown error", e);
        }

        String fileName = "codis-" + String.valueOf(System.currentTimeMillis()) + StatisticalTablesConf.TABLE_FILE_TYPE;
        OutputFileUtils.exportToLocal(fileName, event);
        long endLocalTime=System.currentTimeMillis();
        logger.debug("Export data to local taking " + (endLocalTime - startTime) + "ms.");
        logger.debug("Start to export data to hdfs ...");
        OutputFileUtils.exportToHDFS(fileName);
        long endHdfsTime=System.currentTimeMillis();
        logger.debug("Export data to HDFS taking " + (endHdfsTime - endLocalTime) + "ms.");

        return events.isEmpty();
    }

    @Override
    public boolean produceEvent(List<String> event) {
        try {
            events.put(event);// 向盘子末尾放一个鸡蛋，如果盘子满了，当前线程阻塞
        } catch (Exception e) {
            logger.error("Unknown error", e);
            return false;
        }

        return true;
    }


}
