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
import "./Navbar.css"
import {Container, Navbar as MyNavbar, Button, Dropdown, Row,Col} from "react-bootstrap";
import { Link } from "react-router-dom";
import { FaHome } from "react-icons/fa";
import logo from "../../../logo.svg"

const Navbar = (props) => {

    return (
        <MyNavbar collapseOnSelect bg="light" variant="light" className="shadow p-2 bg-white sticky-top">
                <Container fluid>
                    <MyNavbar.Brand as={Link} to="/" data-testid='home-logo-button'>
                        <img src={logo} alt="Logo Icon" width={40} height={40} />
                    </MyNavbar.Brand>
                    <Link to={"/"} data-testid='home-icon-button'>
                        <FaHome className="home-icon-navbar" />
                    </Link>
                    <>
                        <Link to={"/signup"}>
                            <Button variant="secondary" className="btn-navbar mb-2 mb-sm-0">
                                SignUp
                            </Button>
                        </Link>
                        <Link to={"/login"}>
                            <Button className="btn-navbar mx-sm-2 justify-content-end">Login</Button>
                        </Link>
                    </>
                </Container>
        </MyNavbar >
    );
};

export default Navbar;
