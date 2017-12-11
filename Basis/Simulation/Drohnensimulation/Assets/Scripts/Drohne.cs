using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Drohne : MonoBehaviour {

    private Vector3 velocity = Vector3.zero;
    private Vector3 rotation = Vector3.zero;
    private Rigidbody rb;

    public float speed = 5f;
    public float aspeed = 0.5f;
	// Use this for initialization
	void Start () {
        rb = GetComponent<Rigidbody>();
	}
	
	// Update is called once per frame
	void Update () {
        float _xMov = Input.GetAxisRaw("Horizontal");
        float _zMov = Input.GetAxisRaw("Vertical");
        float _lMov = Input.GetAxisRaw("Level");
        Vector3 moveHori = transform.right * _xMov;
        Vector3 moveVert = transform.forward * _zMov;
        Vector3 moveLevel = transform.up * _lMov;

        Vector3 velo = (moveHori + moveVert + moveLevel).normalized * speed;
        Move(velo);
        //Rotation

        float _up = Input.GetAxisRaw("RotUp");
        float _rot = Input.GetAxisRaw("Rotation");

        rotation = ((Vector3.up * _rot)+(Vector3.right*_up)).normalized * aspeed;
        
	}

    public void Move(Vector3 _velocity)
    {
        velocity = _velocity;
    }

    private void FixedUpdate()
    {
        if (velocity != Vector3.zero)
            rb.MovePosition(transform.position + velocity * Time.fixedDeltaTime);
        if (rotation != Vector3.zero)
            transform.Rotate(rotation);
    }
}
