/*
 * --------------------------------------------------------------------
 *
 * Package:         client
 * Module:          src/views/Dashboard
 * File:            Home.jsx
 *
 * Copyright (c) 2022 - se2022-Team12
 * All rights reserved.
 * --------------------------------------------------------------------
 */

//Imports
import { Row, Col } from "react-bootstrap";
import ChatComponent from "../../components/ui-core/ChatComponents/ChatComponent";

const Chat = () => {
    // useEffect(() => {
    //     Api.getAllProduct()
    //         .then(products =>{
    //             setProducts(products);
    //         })
    //         .catch( err =>{
    //             notify.error("Server error")
    //         })
    // }, []); //eslint-disable-line react-hooks/exhaustive-deps

    return (
        <>
            <div className="mt-4 d-flex flex-column justify-content-center align-items-center">
                <Row className="w-75">
                    <Col align="center">
                        <h1 className="fw-bold fst-italic mt-4">CHAT PAGE</h1>
                    </Col>
                </Row >
                <Row className="w-75">
                    <ChatComponent/>
                </Row>
            </div>
        </>
    );
};

export default Chat;
