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
import {Container, Table} from "react-bootstrap";
import {useContext} from "react";
import {AuthContext} from "../../utils/AuthContext";
import {Button} from "react-bootstrap";
import {useNavigate} from "react-router-dom";

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
                        {props.products.map((product) =>
                            <ProductRow key ={product.id} product = {product}/>
                        )}

                    </tbody>
                </Table>
            </Container>

        </>
    );
};

function ProductRow(props){
    //console.log(props.product)
    return(
        <tr>
            <ProductData product = {props.product}/>
        </tr>
    )
}

function ProductData(props){
    const authContext = useContext(AuthContext);
    const navigate  = useNavigate();
    return(
        <>
            <td>
                {props.product.id}
            </td>
            <td>
                {props.product.name}
            </td>
            <td>
                {props.product.brand}
            </td>
            <td>
                {props.product.description}
            </td>
            {authContext.user.role == 'manager'?
                <td><Button onClick={()=>{navigate(`/product/${props.product.id}`)}}>Edit</Button></td>:
                <></>}
        </>
    )
}

export default ProductTable;
