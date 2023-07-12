import Button from "react-bootstrap/Button";
import Card from "react-bootstrap/Card";
import ListGroup from "react-bootstrap/ListGroup";
import Api from "../../../services/Api";
import { Badge, Row, Col } from "react-bootstrap";
import Dropdown from "react-bootstrap/Dropdown";
import DropdownButton from "react-bootstrap/DropdownButton";

const card = {
	id: 1,
	productId: "A01",
	category: "assist",
	priority: 1,
	description: "problem description",
	profileId: 31,
	expertId: null,
	status: "OPEN",
};

const priorityMap = {
	1: {
		name: "low",
		status: "success",
	},
	2: {
		name: "medium",
		status: "warning",
	},
	3: {
		name: "high",
		status: "danger",
	},
};

const TicketCard = (props) => {
	return (
		<Card style={{ width: "40rem" }}>
			<Card.Body>
				<Card.Title>
					<Row align="left">
						<Col>
							<b>Ticket {card.id}</b>
						</Col>

						<Col align="right">
							<Badge bg={"success"}>Available</Badge>
						</Col>
					</Row>
				</Card.Title>
				<ListGroup variant="flush">
					<ListGroup.Item>
						<Row align="left">
							<Col>
								<b>ProductId</b>
							</Col>

							<Col align="right">{card.productId}</Col>
						</Row>
					</ListGroup.Item>
					<ListGroup.Item>
						<Row align="left">
							<Col>
								<b>category</b>
							</Col>

							<Col align="right">{card.category}</Col>
						</Row>
					</ListGroup.Item>
					<ListGroup.Item>
						<Row align="left">
							<Col>
								<b>Priority</b>
							</Col>

							<Col align="right">
								<DropdownButton id="dropdown-basic-button" title="Change Priority" size="sm">
									<Dropdown.Item href="#/action-1">
										<Badge bg={priorityMap[1].status}>
											{priorityMap[1].name}
										</Badge>
									</Dropdown.Item>
									<Dropdown.Item href="#/action-2">
										<Badge bg={priorityMap[2].status}>
											{priorityMap[2].name}
										</Badge>
									</Dropdown.Item>
									<Dropdown.Item href="#/action-3">
										<Badge bg={priorityMap[3].status}>
											{priorityMap[3].name}
										</Badge>
									</Dropdown.Item>
								</DropdownButton>
							</Col>
						</Row>
					</ListGroup.Item>
					<ListGroup.Item>
						<Row align="left">
							<Col>
								<b>ProfileId</b>
							</Col>

							<Col align="right">{card.profileId}</Col>
						</Row>
					</ListGroup.Item>
					<ListGroup.Item>
						<Row align="left">
							<Col>
								<b>ExpertId</b>
							</Col>

							<Col align="right">
								<DropdownButton id="dropdown-basic-button" title="Assign Expert" size="sm">
									<Dropdown.Item href="#/action-1">
										<Badge bg={priorityMap[1].status}>
											{priorityMap[1].name}
										</Badge>
									</Dropdown.Item>
									<Dropdown.Item href="#/action-2">Another action</Dropdown.Item>
									<Dropdown.Item href="#/action-3">Something else</Dropdown.Item>
								</DropdownButton>
							</Col>
						</Row>
					</ListGroup.Item>
				</ListGroup>
				<Card.Text>{card.description}</Card.Text>
                <ListGroup>
                <Row align="left">
							<Col>
								<Button variant="success">Save</Button>
							</Col>

							<Col align="right"><Button variant="danger">Close</Button></Col>
						</Row>
                </ListGroup>
			</Card.Body>
		</Card>
	);
};

export default TicketCard;
