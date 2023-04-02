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
import {TbError404} from "react-icons/tb";
import {Link} from "react-router-dom";
import {Button, Row} from "react-bootstrap";
const Error = () => {
    return (
        <>
            <Row className='p-5 m-5 flex-fill text-dark align-items-center'>
                <div className='d-flex flex-column align-items-center'>
                    <TbError404 style={{ fontSize: '200px' }} className='me-3' />
                    <h3 className='mb-0 fw-bold'>Page Not Found</h3>
                    <div className='my-5'>
                        <Link to={'/'}>
                            <Button size='xs'>Back to home</Button>
                        </Link>
                    </div>
                </div>
            </Row>
        </>
    );
}

export default Error;