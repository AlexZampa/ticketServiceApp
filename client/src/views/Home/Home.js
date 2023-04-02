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
const Home = () => {
    return (
        <>
            <div className='mt-4 d-flex flex-column justify-content-center align-items-center'>
                <h1 className='text-center fw-bold fst-italic mt-4'>
                    PRODUCT LIST
                </h1>
                <ProductTable/>
            </div>
        </>
    );
}

export default Home;