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
import {Container, Navbar as MyNavbar, Button, Dropdown, Row, Col, Form, Table} from "react-bootstrap";
import {Link} from "react-router-dom";

const ProductTable = (props) => {

    return (
        <>
            <Container>
                <Table responsive="sm" hover>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Brand</th>
                        <th>Description</th>
                    </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </Table>
            </Container>

        </>
    );
};

export default ProductTable;
