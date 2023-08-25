import React, {useState, useCallback, useEffect} from 'react'
import {Button, Col, Form, FormGroup, InputGroup, Row} from 'react-bootstrap'
import {Download} from "react-bootstrap-icons";
import {AuthContext} from "../../utils/AuthContext";
import {useContext} from "react";
import Api from "../../../services/Api";
import useNotification from "../../utils/useNotification";
import {BiRefresh} from "react-icons/bi";




const ChatComponent = (props) => {
    const authContext = useContext(AuthContext);
    const notify  = useNotification();
    const [files,setFiles] = useState(null)

    const [messages, setMessages] = useState([])
    const [message, setMessage] = useState('')
    const [ticket,setTicket] = useState(undefined)

    useEffect(()=>{
        Api.getMessages(props.ticketId,authContext.user.token)
            .then(m =>{
                setMessages(m);
            } )
            .catch(err =>{notify.error('Database Error')})

        Api.getTicketById(props.ticketId,authContext.user.token)
            .then(t =>{
            setTicket(t);
        } )
            .catch(err =>{notify.error('Database Error')})

    },[])

    const refresh = ()=>{
        setMessage("")
        setFiles(null)
        Api.getMessages(props.ticketId,authContext.user.token)
            .then(m =>{
                setMessages(m);
            } )
            .catch(err =>{notify.error('Database Error')})

        Api.getTicketById(props.ticketId,authContext.user.token)
            .then(t =>{
                setTicket(t);
            } )
            .catch(err =>{notify.error('Database Error')})

    }


    const handleDownload = async (attId) => {
        var file;
        for (file of attId) {
            await Api.getAttachment(props.ticketId, file.id, authContext.user.token)
                .then(d => {
                    const url = window.URL.createObjectURL(d);
                    const link = document.createElement("a");
                    link.href = url;
                    link.setAttribute("download", file.name); //or any other extension
                    document.body.appendChild(link);
                    link.click();
                    this.setState({downloading: false});
                }).catch(err => {

                })
        }
    }

    const handleSend = () => {



        const m = {
            "ticketId": props.ticketId,
            "senderId": authContext.user.id,
            "receiverId": ticket.expertId,
            "text": message,
        }
        
        const formData = new FormData();
        formData.append('ticketId', m.ticketId);
        formData.append('senderId', m.senderId);
        formData.append('receiverId', m.receiverId);
        formData.append('text', m.text);
        if(files){
            const fileArray = [...files]
            for (var i = 0; i < fileArray.length; i++) {
                formData.append('attachments', fileArray[i]);
            }
        }
        else{
            formData.append('attachments', new Blob());
        }

        Api.addNewMessage(props.ticketId,formData,authContext.user.token)
            .then(() => {
                refresh()

            })
            .catch(err => {})
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
                                            <Download className='m-2' onClick={()=>{handleDownload(message.attachments)}}/>
                                        </Row>
                                        :<></>}

                                </div>
                                <div className={`text-muted small ${message.senderId == authContext.user.id ? `text-right` : ``} `}>
                                    {message.senderId == authContext.user.id ? 'You' : 'Other'}
                                </div>

                            </div>
                        )
                    })}
                </div>
            </div>
            <Form >
                <Row className="d-flex">
                    <Col xs={1}>
                        <Button style={{height: "50px"}} className="mt-2 ms-2 pe-3 ps-3 " ><BiRefresh onClick={refresh}>Refresh</BiRefresh></Button>
                    </Col >

                    <Col xs={11}>
                        <FormGroup className="m-2">
                            <InputGroup>
                                <Form.Control as="textarea"
                                              required value={message}
                                              onChange={e => setMessage(e.target.value)}
                                              style={{height: "50px", resize: "none"}}/>
                                <Button onClick={handleSend}>Send</Button>
                            </InputGroup>
                        </FormGroup>
                    </Col>
                </Row>

                <Form.Group controlId="formFileMultiple" className="mb-3">
                    <Form.Control type="file" multiple onChange={e => setFiles(e.currentTarget.files)}/>
                </Form.Group>
            </Form>
            </div>
    )
}

export default ChatComponent;