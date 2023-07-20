import React, {useState, useCallback, useEffect} from 'react'
import {Button, Form, FormGroup, InputGroup, Row} from 'react-bootstrap'
import {Download} from "react-bootstrap-icons";
import {AuthContext} from "../../utils/AuthContext";
import {useContext} from "react";
import useWebSocket from 'react-use-websocket';

const WS_URL = 'ws://localhost:8080/chat';

const messages = [
    {
        text:"prova1 dsdklcnsdklcnsdlkcnsdklcnsdklcnsdlcnsdlkcnsd sdlkncsdklcn scbnklsncskld ncklsdnsdl",
        fromMe:true
    },
    {
        text:"prova2",
        fromMe:false
    },
    {
        text:"prova3",
        fromMe:true
    },
    {
        text:"prova4",
        fromMe:true
    },

]


const ChatComponent = (props) => {
    const [ws,setWebsocket] = useState(null);
    const [text,setText]=useState('')
    const authContext = useContext(AuthContext);

    const [messages, setMessages] = useState([])
    const [user, setUser] = useState(null)

    useEffect(()=>{
        setWebsocket(()=>{
            const ws = new WebSocket(WS_URL);
        })
    },[])


    console.log("web soccket", ws)


    return(
        <div className="d-flex flex-column flex-grow-1">
            <div className="flex-grow-1 overflow-auto">
                <div className="d-flex flex-column align-items-start justify-content-end px-3">

                    {messages.map((message, index) => {
                        const lastMessage = messages.length - 1 === index
                        return (
                            <div
                                ref={ null}
                                key={index}
                                className={`my-1 d-flex flex-column ${message.fromMe ? `align-self-end` : ''}`}
                            >
                                <div
                                    className={`rounded px-2 py-1 ${message.fromMe ? `bg-primary  text-white` : `bg-success text-white`}`}>
                                    <Row className='ms-2 me-2'>
                                        {message.text}
                                    </Row>
                                    <Row className='m-2 justify-content-center rounded bg-gradient' >
                                        download attachment
                                        <Download className='m-2' onClick={()=>{console.log('download')}}/>
                                    </Row>

                                </div>
                                <div className={`text-muted small ${message.fromMe ? `text-right` : ``} `}>
                                    {message.fromMe ? 'You' : 'Other'}
                                </div>

                            </div>
                        )
                    })}
                </div>
            </div>
            <Form >
                <FormGroup className="m-2">
                    <InputGroup>
                        <Form.Control as="textarea"
                                      required value={text}
                                      onChange={e => setText(e.target.value)}
                                      style={{height: "50px", resize: "none"}}/>
                            <Button type="submit">Send</Button>
                    </InputGroup>
                </FormGroup>
            </Form>
        </div>
    )
}

export default ChatComponent;