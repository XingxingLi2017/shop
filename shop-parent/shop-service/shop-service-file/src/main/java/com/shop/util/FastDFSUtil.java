package com.shop.util;

import com.shop.file.FastDFSFile;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/***
 * manage FastDFS
 */
public class FastDFSUtil {

    /***
     * static init code block , load fdfs_client.conf file
     */
    static {
        try {
            String filename = new ClassPathResource("fdfs_client.conf").getPath();
            ClientGlobal.init(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * upload file
     * @param file file uploaded by user
     * @return String[] , group name and file name in storage node
     */
    public static String[] uplaod(FastDFSFile file) throws IOException, MyException {
        TrackerServer trackerServer = getTrackerServer();

        // get storage server info , create storage client
        StorageClient storageClient = new StorageClient(trackerServer, null);

        // store file into storage server by storage client
        /*
        * parameters : file content bytes, file extension, additional info( key-value pairs )
        * */
        String[] ackInfo = storageClient.upload_file(file.getContent(),
                                    file.getExt(),
                                    new NameValuePair[]{
                                            new NameValuePair("author", file.getAuthor()),
                                            new NameValuePair("name", file.getName())});

        // get ack info from storage
        return ackInfo;
    }

    private static TrackerServer getTrackerServer() throws IOException {
        // get fast tracker client
        TrackerClient trackerClient = new TrackerClient();

        // access tracker server to get info
        return trackerClient.getConnection();
    }

    /****
     * get file info
     * @param groupName
     * @param remoteFileName
     */
    public static FileInfo getFile(String groupName, String remoteFileName) throws IOException, MyException {
        TrackerServer trackerServer = getTrackerServer();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        return storageClient.get_file_info(groupName, remoteFileName);
    }

    public static InputStream download(String groupName, String remoteFileName) throws IOException, MyException {
        TrackerServer trackerServer = getTrackerServer();
        StorageClient storageClient = new StorageClient(trackerServer, null);

        byte[] file = storageClient.download_file(groupName, remoteFileName);
        return new ByteArrayInputStream(file);
    }

    public static void delete(String groupName, String remoteFileName) throws IOException, MyException {
        TrackerServer trackerServer = getTrackerServer();
        StorageClient storageClient = new StorageClient(trackerServer, null);

        storageClient.delete_file(groupName, remoteFileName);
    }

    /****
     * get storage server info
     * @return store_path_index
     */
    public static StorageServer getStorage() throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerClient.getStoreStorage(trackerServer);
    }

    /***
     * get storage's info of storage storing the stored file
     */
    public static ServerInfo[] getServerInfo(String groupName, String remoteFileName) throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerClient.getFetchStorages(trackerServer, groupName, remoteFileName);
    }

    /***
     * get tracker's info
     */
    public static String getTrackerInfo() throws IOException {
        TrackerServer trackerServer = getTrackerServer();

        String ip = trackerServer.getInetSocketAddress().getHostString();
        // get http port
        int trackerHttpPort = ClientGlobal.getG_tracker_http_port();

        String url = "http://" + ip + ":" + trackerHttpPort;
        return url;
    }

    public static void main(String[] args) throws IOException, MyException {
//        FileInfo fileInfo = getFile("group1", "M00/00/00/wKhNjF_ElQiAPkGcABU5AaBGayk705.jpg");
//        System.out.println(fileInfo.toString());

//        InputStream is = download("group1", "M00/00/00/wKhNjF_ElQiAPkGcABU5AaBGayk705.jpg");
//        FileOutputStream fos = new FileOutputStream("C:\\Users\\Xingxing Li\\Desktop\\1.jpg");
//        byte[] buffer = new byte[1024];
//        while(is.read(buffer) != -1) {
//            fos.write(buffer);
//        }
//        fos.flush();
//        fos.close();
//        is.close();

//        delete("group1", "M00/00/00/wKhNjF_Eny6AME7bABWN41qTh8c47.jpeg");
//        ServerInfo[] groups = getServerInfo("group1", "M00/00/00/wKhNjF_ElQiAPkGcABU5AaBGayk705.jpg");
//        for(ServerInfo info : groups) {
//            System.out.println(info.getIpAddr());
//            System.out.println(info.getPort());
//        }
    }
}
