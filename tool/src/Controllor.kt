import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import io.netty.bootstrap.Bootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.EventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.channel.ChannelOption
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelHandlerContext
import io.netty.buffer.ByteBuf

class Controllor {

    private val thdPool: EventLoopGroup = NioEventLoopGroup()

    private val bs = Bootstrap().group(thdPool)
            ?.channel(javaClass<NioSocketChannel>())
            ?.option(ChannelOption.SO_KEEPALIVE, true)
            ?.handler(object: ChannelInitializer<SocketChannel>() {
                override fun initChannel(ch: SocketChannel?) {
                    ch!!.pipeline()!!.addLast(
                            object: ChannelInboundHandlerAdapter() {
                                override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
                                    msg as ByteBuf
                                    val buf = ByteArray(msg.readableBytes())
                                    msg.readBytes(buf)
                                    logText!!.appendText(String(buf))
                                }
                            })
                }
            })

    FXML
    var inputText: TextArea? = null

    FXML
    var logText: TextArea? = null

    FXML
    var address: TextField? = null

    FXML
    var port: TextField? = null

    FXML
    fun send(e: ActionEvent) {

    }

    FXML
    fun clear(e: ActionEvent) {

    }

    FXML
    fun connect(e: ActionEvent) {
        bs!!.connect(address!!.getText(), port!!.getText()!!.toInt())
    }
}