/*
* --------------------------------------------------------------------
*
* Package:         client
* Module:          src/views/Home
* File:            Home.jsx
*
* Copyright (c) 2022 - se2022-Team12
* All rights reserved.
* --------------------------------------------------------------------
*/

//Imports
import ProductTable from "../../components/ui-core/ProductTable/ProductTable";
import Api from "../../services/Api";
import React, { useState, useEffect } from 'react';
import {Button, Col, Form, Row} from "react-bootstrap";
import { BsSearch } from "react-icons/bs";
import useNotification from "../../components/utils/useNotification";
const Home = () => {

    const [products, setProducts] = useState([]);
    const [search, setSearch] = useState("");
    const notify = useNotification()

    useEffect(() => {
        Api.getAllProduct()
            .then(products =>{
                setProducts(products);
            })
            .catch( err =>{
                notify.error("Server error")
            })
    }, [products]); //eslint-disable-line react-hooks/exhaustive-deps
    const handleSearch = (event) =>{
        event.preventDefault();
        if (search === "" || search.includes(" ")) {
            Api.getAllProduct()
                .then(products =>{
                    setProducts(products);
                })
                .catch( err =>{
                    notify.error("Server error")
                })
            return;
        }

        Api.getProduct(search)
            .then(product =>{
                let vproduct = [];
                vproduct.push(product);
                setProducts(vproduct);
            })
            .catch( err =>{
                notify.error("Product not found")
            })

    }
    return (
        <>
            <div className='mt-4 d-flex flex-column justify-content-center align-items-center'>
                <Row className="w-75">
                    <Col className="justify-content-start col-8">
                        <h1 className='fw-bold fst-italic mt-4'>
                            PRODUCT LIST
                        </h1>
                    </Col>
                    <Col className="justify-content-end mt-4">
                        <Form className="d-flex">
                            <Form.Control
                                type="search"
                                placeholder="Search"
                                className="me-2"
                                aria-label="Search"
                                data-testid="name-select"
                                onChange={(event) => { setSearch(event.target.value) }}

                            />
                            <Button type="submit" className='fw-bold fst-italic' onClick={event => handleSearch(event)}>
                                <BsSearch className='p-0 m-0'/>
                            </Button>
                        </Form>
                    </Col>
                </Row>
                    <ProductTable products={products}/>
            </div>
        </>
    );
}

export default Home;