/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import React from "react";
import { MDBInput } from "mdbreact";
import { InputText } from "primereact/inputtext";

const TextField = () => {
    return (
            <div className="p-col-12 p-md-4">
                <div className="p-inputgroup">
                    <span className="p-inputgroup-addon">
                        <i className="pi pi-user"></i>
                    </span>
                    <InputText placeholder="Text" />
                </div>
            </div>
            );
}

export default TextField;