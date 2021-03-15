/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global Intl */

import React, { Component } from 'react'
import formatDate from '../util/formatDate.js';
import './Timestamp.css';

export default function Timestamp(props) {



    const data = props.data;
    // Format the date string by removing the last 5 characters ([UTC])
    let timestamp = formatDate(data.createdAt.substring(0,data.createdAt.length-5));
    //let date = new Date().toLocaleString();

    return (
            <div className="timestamp p-d-flex p-ai-center">
                <span className="pi pi-calendar p-mr-1"></span>
                {timestamp}
            </div>
            );
}
