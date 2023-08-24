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
import ProductCreateComponent from "../../components/ui-core/ProductCreatComponent/ProductCreateComponent";
import {useNavigate} from "react-router-dom";
const ProductCreate = () => {
    const authContext = useContext(AuthContext);
    const params = useParams();
    const [products,setProducts] = useState([])
    const notify = useNotification()
    const navigate = useNavigate()
    console.log(params)

    useEffect(() => {

        if(authContext.user.role != 'manager'){
            notify.error('you are not a manager')
            navigate('/')
        }

    }, []);


    return (
        <>
            <div className="mt-4 d-flex flex-column justify-content-center align-items-center">
                <Row className="w-100">
                    <Col align="center">
                        <h1 className="fw-bold fst-italic mt-4">{params.productId?"MODIFY PRODUCT":"CREATE NEW PRODUCT"}</h1>
                    </Col>
                </Row>
                <Row className="w-75">
                    <ProductCreateComponent products={products}/>
                </Row>
            </div>
        </>
    );
};

export default ProductCreate;
