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
import Api from "../../services/Api";
import { Row, Col } from "react-bootstrap";
import { useParams } from "react-router-dom";
import TicketCard from "../../components/ui-core/TicketComponent/TicketCard";
import {useContext, useEffect, useState} from "react";
import {AuthContext} from "../../components/utils/AuthContext";
import useNotification from "../../components/utils/useNotification";
import TicketCreateComponent from "../../components/ui-core/TicketCreateComponent/TicketCreateComponent";


const TicketCreate = () => {
    const authContext = useContext(AuthContext);
    const params = useParams();
    const [products,setProducts] = useState([])
    const notify = useNotification()

    useEffect(() => {

        Api.getAllProduct()
            .then(products =>{
                setProducts(products);
            })
            .catch( err =>{
                notify.error("Server error")
            })
    }, []);


    return (
        <>
            <div className="mt-4 d-flex flex-column justify-content-center align-items-center">
                <Row className="w-75">
                    <Col align="center">
                        <h1 className="fw-bold fst-italic mt-4">CREATE NEW TICKET</h1>
                    </Col>
                </Row>
                <Row>
                    <TicketCreateComponent products={products}/>
                </Row>
            </div>
        </>
    );
};

export default TicketCreate;
