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
import {Button, Form, Navbar as MyNavbar} from "react-bootstrap";
import {PersonCircle, Ticket} from 'react-bootstrap-icons';

const Navbar = () => {

    return (
        <MyNavbar expand="lg" variant="dark" fixed="top" className='bg-black w-100'>
            <MyNavbar.Brand href="/" className='ms-2'>
                <Ticket/>
                <span className="ms-2"> Ticket Service</span>
                
            </MyNavbar.Brand>
            <MyNavbar.Brand href="profile" className='justify-content-end logout-text ms-lg-auto'>
                <PersonCircle className='person-circle' color='white' size={25} />
                <Button size='sm' className= "ms-2" variant={'secondary '} >
                    Profile
                </Button>
            </MyNavbar.Brand>
        </MyNavbar>
    );
};

export default Navbar;
