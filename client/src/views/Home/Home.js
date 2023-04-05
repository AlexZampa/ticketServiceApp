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
import { useState, useEffect } from 'react';
import {Button, Col, Form, Row} from "react-bootstrap";
import { BsSearch } from "react-icons/bs";
const Home = () => {

    const [products, setProducts] = useState([]);
    const [search, setSearch] = useState(null);

    useEffect(() => {
        Api.getAllProduct()
            .then(products =>{
                setProducts(products);
            })
            .catch( err =>{
                console.log('errore'+ err);
            })
    }, []); //eslint-disable-line react-hooks/exhaustive-deps
    const handleSearch = () =>{
        Api.getProduct(search)
            .then(product =>{
                let vproduct = [];
                vproduct.push(product);
                setProducts(vproduct);
            })
            .catch( err =>{
                console.log('errore'+ err);
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
                    <Col className="justify-content-end">
                        <Form className="mt-4">
                            <Form.Control
                                data-testid="name-select"
                                type="text"
                                placeholder="Search"
                                onChange={(event) => { setSearch(event.target.value) }}
                                //value={hutName}
                            />
                        </Form>
                    </Col>
                    <Col className="justify-content-end p-0">
                        <Button className=' fw-bold fst-italic mt-4' onClick={() => handleSearch()}>
                            <BsSearch className='p-0 m-0'/>
                        </Button>
                    </Col>
                </Row>
                {search?
                    <ProductTable products={products}/>
                    :
                    <ProductTable products={products}/>}

            </div>
        </>
    );
}

export default Home;