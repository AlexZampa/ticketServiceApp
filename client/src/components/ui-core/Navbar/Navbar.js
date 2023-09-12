/*
 * --------------------------------------------------------------------
 *
 * Package:         client
 * Module:          src/components/ui-core/Navbar
 * File:            Navbar.js
 *
 * --------------------------------------------------------------------
 */

//Imports
import 'bootstrap/dist/css/bootstrap.min.css';
import {Button, Col, Navbar as MyNavbar, Row} from "react-bootstrap";
import {PersonCircle, Ticket} from 'react-bootstrap-icons';
import {useContext} from "react";
import {AuthContext} from "../../utils/AuthContext";
import {useNavigate} from "react-router-dom";


const Navbar = () => {
    const authContext = useContext(AuthContext);
    const navigate = useNavigate();

    return (
        <MyNavbar expand="lg" variant="dark" fixed="top" className='bg-black w-100'>
            <MyNavbar.Brand href="/" className='ms-2'>
                <Ticket/>
                <span className="ms-2"> Ticket Service</span>
            </MyNavbar.Brand>
                <Button className='ms-4' variant={'secondary '} onClick={()=>{navigate('/dashboard')}}>Dashboard</Button>

                {authContext.user.role == 'manager'?<Button className='ms-4' variant={'secondary '}  onClick={()=>{navigate('/product/create')}}>Create Product</Button>:<></>}
                {authContext.user.role == 'user'?<Button className='ms-4' variant={'secondary '}  onClick={()=>{navigate('/ticket/create')}}>Create Ticket</Button>:<></>}
<Row  className='justify-content-end logout-text ms-lg-auto me-2'>
    <Col className='me-0 p-0'>
        <PersonCircle className='person-circle ' color='white' size={35} onClick={()=>{navigate('/profile')}}/>
    </Col>
    <Col className='ms-1 p-0 me-2'>

        {
            authContext.user.token ?
                <Button className="ms-2" variant={'secondary '} onClick={()=>{navigate('/profile')}}>
                    {authContext.user.role}
                </Button>
                :
                <Button className="ms-2" variant={'secondary '} onClick={()=>{navigate('/login')}}>
                    login
                </Button>
        }
    </Col>

            </Row>
        </MyNavbar>
    );
};

export default Navbar;
