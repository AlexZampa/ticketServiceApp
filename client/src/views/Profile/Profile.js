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
import ProfileContent from "../../components/ui-core/ProfileContent/ProfileContent";
const Profile = () => {
    return (
        <>
            <div className='mt-4 d-flex flex-column justify-content-center align-items-center'>
                <ProfileContent/>
            </div>
        </>
    );
}

export default Profile;