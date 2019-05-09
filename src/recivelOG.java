
import java.net.DatagramPacket;
import java.net.DatagramSocket;
/**
 * 为避免旧生产集群Flume采集数据时发生端口占用问题，转发至本机其他端口。
 * 即：数据源发送数据到 服务器A 的 p1端口，转发脚本采集 服务器A 的 p1 端口的数据然后转发数据至 服务器A 的 P2 端口 和 服务器B 的p1 端口
 * 旧的生产集群从p2接收数据，新集群从p1接收数据
 * */
public class recivelOG {
    public void Server(){
        try {
            //System.out.println("start:开始接受数据");
            /**
             * DatagramSocket(int prot)
             * 创建一个DatagramSocket实例，并将该对象绑定到本机默认IP地址、指定端口_采集监听的数据
             * 默认端口为 514
             * */
            DatagramSocket socket = new DatagramSocket(5140);
            while(true){
                //存储接收的数据
                byte[] buf = new byte[2048];
                /**
                 * DatagramPacket(byte[] buf, int offset, int length)
                 * 以一个空数组来创建DatagramPacket对象，并指定接收到的数据放入buf数组中时从offset开始，最多放length个字节。
                 * 用来存储接受的数据
                 * */
                DatagramPacket packet = new DatagramPacket(buf,buf.length);
                // 接收数据包
                socket.receive(packet);
                byte[] data = packet.getData();
                // 去除转发数据中的物理信息 主机名 时间 日期等
                String msg = new String(data,42,packet.getLength());

                System.out.println(msg);
                /**
                 * 创建一个发送数据的DatagramPacket对象
                 * DatagramPacket packet = new DatagramPacket(buf, length, address, port);
                 * 发送数据报
                 * socket.send(packet);
                 * */
                //socket.send(new DatagramPacket(msg.getBytes(),msg.length(), InetAddress.getByName("159.226.16.161"),5141));
                //socket.send(new DatagramPacket(msg.getBytes(),msg.length(), InetAddress.getByName("192.168.16.4"),514));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    public static void main(String[] args) {
        new Thread(){
            @Override
            public void run() {
                recivelOG server = new recivelOG();
                server.Server();
            }
        }.start();
    }
}
