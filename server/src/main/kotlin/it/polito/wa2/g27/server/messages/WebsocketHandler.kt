import org.json.JSONException
import org.json.JSONObject
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.socket.*
import org.springframework.web.socket.config.annotation.*
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.io.IOException
import java.time.LocalTime


@Component
class TextHandler : TextWebSocketHandler() {

    val sessions = mutableListOf<WebSocketSession>()
    @Throws(InterruptedException::class, IOException::class, JSONException::class)
    public override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val payload = message.payload
        val jsonObject = JSONObject(payload)

        session.sendMessage(TextMessage("Your message:" + jsonObject.get("user")))
    }

    @Throws(Exception::class)
    override fun afterConnectionEstablished(session: WebSocketSession) {
        println("Server connection opened")

        sessions.add(session)
        val message = TextMessage("one-time message from server")
        println("Server sends: ${message}")
        session.sendMessage(message)
    }



    @Scheduled(fixedRate = 10000)
    @Throws(IOException::class)
    fun sendPeriodicMessages() {
        for (session in sessions) {
            if (session.isOpen) {
                val broadcast = "server periodic message " + LocalTime.now()
                println("Server sends: ${broadcast}")
                session.sendMessage(TextMessage(broadcast))
            }
        }
    }
}

@Configuration
@EnableWebSocket
class WSConfig : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(TextHandler(), "/chat").withSockJS()
    }


}