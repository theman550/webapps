import React, { Component } from 'react'
import "./About.css"
import reddrinkfull from "../images/reddrinkfull.png"

export default class About extends Component {
    render() {
        return (
                <div className="about-page">
                    <h1>About</h1>
                    <p>Ever wanted to mix that perfect drink, but didn't know a good recipe? Maybe you are a connoisseur of drinks, and want to share your wonderful experiences? Regardless, we as students from Chalmers encourage a hefty amount of drinking, so come have a drink or two.</p>
                    <img src={reddrinkfull} alt="image of logo" width="400rem"></img>
                    <p>Created by:
                        <br/> <a href="https://github.com/ousama123">Oussama Anadani</a>
                        <br/> <a href="https://github.com/adrianhak">Adrian Håkansson</a>
                        <br/> <a href="https://github.com/IsakGMagnusson">Isak Magnusson</a>
                        <br/> <a href="https://github.com/theman550">Jacob Spilg</a>
                        <br/> <a href="https://github.com/slnxc">Alexander Tepic</a> 
                    </p>
                </div>
                )
    }
}
