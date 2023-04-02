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
import {Container, Navbar as MyNavbar, Button, Dropdown, Row,Col,Form} from "react-bootstrap";
import { Link } from "react-router-dom";
import { FaHome } from "react-icons/fa";
import logo from "../../../logo.svg"
import { Film, PersonCircle, Ticket } from 'react-bootstrap-icons';

const Navbar = (props) => {

    return (
        <MyNavbar expand="lg" variant="dark" fixed="top" className='bg-black'>
            <MyNavbar.Brand href="#home" className='ms-2'>
                <Ticket/>
                <span className="ms-2"> Ticket Service</span>
                
            </MyNavbar.Brand>
            <MyNavbar.Collapse>
                <Form className='mx-auto'>
                    <Form.Group>
                        <Form.Control type="text" placeholder="Search" />
                    </Form.Group>
                </Form>
            </MyNavbar.Collapse>
            <MyNavbar.Brand href="#account" className='justify-content-end logout-text'>
                <PersonCircle className='person-circle' color='white' size={25} />
                <Button size='sm' className= "ms-2" variant={'secondary '} >
                    Profile
                </Button>
            </MyNavbar.Brand>
        </MyNavbar>
    );
};

export default Navbar;
