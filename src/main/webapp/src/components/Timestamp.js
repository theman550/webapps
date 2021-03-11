/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global Intl */

import React, { Component } from 'react'
import './Timestamp.css';
export default function Timestamp(props) {
    const data = props.data;
    let timestamp = data.createdAt;
    //let date = new Date().toLocaleString();

    return (
            <div className="p-col-12">
                <a>{timestamp}</a>
            
            </div>
            );
}
