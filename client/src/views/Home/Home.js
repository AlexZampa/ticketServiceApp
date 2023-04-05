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
const Home = () => {

    const [products, setProducts] = useState([]);

    useEffect(() => {
        Api.getAllProduct()
            .then(products =>{
                setProducts(products);
            })
            .catch( err =>{
                console.log('errore'+ err);
            })
    }, []); //eslint-disable-line react-hooks/exhaustive-deps
    return (
        <>
            <div className='mt-4 d-flex flex-column justify-content-center align-items-center'>
                <h1 className='text-center fw-bold fst-italic mt-4'>
                    PRODUCT LIST
                </h1>
                <ProductTable products={products}/>
            </div>
        </>
    );
}

export default Home;