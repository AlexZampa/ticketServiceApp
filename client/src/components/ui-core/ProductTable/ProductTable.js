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
        </>
    )
}

export default ProductTable;
