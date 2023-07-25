import React, {useState, useCallback, useEffect} from 'react'
import {Button, Form, FormGroup, InputGroup, Row} from 'react-bootstrap'
import {Download} from "react-bootstrap-icons";
import {AuthContext} from "../../utils/AuthContext";
import {useContext} from "react";
import Api from "../../../services/Api";
import useNotification from "../../utils/useNotification";




const ChatComponent = (props) => {
    const authContext = useContext(AuthContext);
    const notify  = useNotification();


    const [messages, setMessages] = useState([])
    const [message, setMessage] = useState('')
    const [ticket,setTicket] = useState(undefined)

    useEffect(()=>{
        Api.getMessages(props.ticketId,authContext.user.token)
            .then(m =>{
                var t;
                console.log(m)
                for( t of m){
                    console.log(t.attachments)
                }
                setMessages(m);
            } )
            .catch(err =>{notify.error('Database Error')})

        Api.getTicketById(props.ticketId,authContext.user.token)
            .then(t =>{
            setTicket(t);
        } )
            .catch(err =>{notify.error('Database Error')})

    },[])



    const handleSend = () => {
        console.log(message)

        const m = {
            "ticketId": props.ticketId,
            "senderId": authContext.user.id,
            "receiverId": ticket.expertId,
            "text": message,
        }
        
        console.log(m)
        const formData = new FormData();
        formData.append('ticketId', m.ticketId);
        formData.append('senderId', m.senderId);
        formData.append('receiverId', m.receiverId);
        formData.append('text', m.text);
        formData.append('attachments', new Blob());

        console.log(formData.get('attachments'))

        for (const [key, value] of formData.entries()) {
            console.log(`Key: ${key}, Value:`, value);
        }

        Api.addNewMessage(props.ticketId,formData,authContext.user.token)
            .then(() => {

            })
            .catch(err => {console.log(err)})
    }

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
                                className={`my-1 d-flex flex-column ${message.senderId == authContext.user.id ? `align-self-end` : ''}`}
                            >
                                <div
                                    className={`rounded px-2 py-1 ${message.senderId == authContext.user.id ? `bg-primary  text-white` : `bg-success text-white`}`}>
                                    <Row className='ms-2 me-2'>
                                        {message.text}
                                    </Row>
                                    {message.attachments.length > 0 && message.attachments[0].size >0  ?
                                        <Row className='m-2 justify-content-center rounded bg-gradient' >
                                            download attachment
                                            <Download className='m-2' onClick={()=>{console.log('download')}}/>
                                        </Row>
                                        :<></>}

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
                                      required value={message}
                                      onChange={e => setMessage(e.target.value)}
                                      style={{height: "50px", resize: "none"}}/>
                            <Button onClick={handleSend}>Send</Button>
                    </InputGroup>
                </FormGroup>
            </Form>
        </div>
    )
}

export default ChatComponent;