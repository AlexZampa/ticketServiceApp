/*
 * --------------------------------------------------------------------
 *
 * Package:         client
 * Module:          src/views/Dashboard
 * File:            Home.jsx
 *
 * Copyright (c) 2022 - se2022-Team12
 * All rights reserved.
 * --------------------------------------------------------------------
 */

//Imports
import DashboardCard from "../../components/ui-core/DashboardComponents/DashboardCard";
import Api from "../../services/Api";
import { Row, Col } from "react-bootstrap";
import {useContext, useEffect, useState} from "react";
import {AuthContext} from "../../components/utils/AuthContext";
import useNotification from "../../components/utils/useNotification";

const Dashboard = () => {
	const authContext = useContext(AuthContext);
	const notify = useNotification()
	const [tickets,setTickets] = useState([])

	useEffect(() =>{
		switch (authContext.user.role) {
			case 'manager':
				Api.getOpenTickets(authContext.user.token)
					.then(tickets =>{
						console.log("questi sono i ticket aperti", tickets)
						setTickets(tickets)
					})
					.catch(err =>{
						console.log(err)
						notify.error("Server error")
					})

				break;
			case 'expert':
				Api.getAssignedTickets(authContext.user.email,authContext.user.token)
					.then(tickets =>{
						setTickets(tickets)
					})
					.catch(err =>{
						console.log(err)
						notify.error("Server error")
					})

				break;
			case 'user':
				Api.getTicketsByProfile(authContext.user.email,authContext.user.token)
					.then(tickets =>{
						console.log(tickets)
						setTickets(tickets)
					})
					.catch(err =>{
						console.log(err)
						notify.error("Server error")
					})

				break;
			default:
				console.log(`it is not possible to change priority`);
		}

		},[])

	return (
		<>
			<div className="mt-4 d-flex flex-column justify-content-center align-items-center">
				<Row className="w-75">
					<Col align="center">
						<h1 className="fw-bold fst-italic mt-4">TICKET DASHBOARD</h1>
					</Col>
				</Row>
				<Row>
					{tickets.length>0 ? tickets.map((ticket)=>{
						return(
							<DashboardCard key={ticket.id} ticket={ticket}/>
						)

					}): <></>}
				</Row>
			</div>
		</>
	);
};

export default Dashboard;
