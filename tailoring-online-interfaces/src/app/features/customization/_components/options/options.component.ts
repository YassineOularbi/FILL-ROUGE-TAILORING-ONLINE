import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import * as THREE from 'three';
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader.js';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js';
import { MaterialType } from '../../../../core/enums/material-type.enum';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-options',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './options.component.html',
  styleUrl: './options.component.scss'
})
export class OptionsComponent implements AfterViewInit {
  materialTypes = Object.values(MaterialType);

  @ViewChild('takchitaCanvas', { static: true }) takchitaCanvas!: ElementRef<HTMLCanvasElement>;
  controls!: OrbitControls;
  camera!: THREE.PerspectiveCamera;

  getMaterialIcon(material: MaterialType): string {
    switch (material) {
      case MaterialType.FABRIC:
        return 'pi pi-image';
      case MaterialType.LACE:
        return 'pi pi-ticket';
      case MaterialType.BUTTON:
        return 'pi pi-circle-on';
      case MaterialType.ACCESSORY:
        return 'pi pi-briefcase';
      case MaterialType.THREAD:
        return 'pi pi-slack';
      case MaterialType.RIBBON:
        return 'pi pi-link';
      case MaterialType.DECORATION:
        return 'pi pi-star';
      default:
        return 'pi pi-question';
    }
  }

  ngAfterViewInit() {
    this.initialize3DModel();
  }

  initialize3DModel() {
    const scene = new THREE.Scene();
    this.camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);
    this.camera.position.z = 2;

    const renderer = new THREE.WebGLRenderer({
      canvas: this.takchitaCanvas.nativeElement,
      antialias: true
    });
    renderer.setSize(window.innerWidth, window.innerHeight);

    const light = new THREE.DirectionalLight(0xffffff, 2);
    light.position.set(2, 2, 2).normalize();
    scene.add(light);

    const loader = new GLTFLoader();
    loader.load('assets/moroccan.glb', (gltf: any) => {
      const model = gltf.scene;
      model.scale.set(1, 1, 1);
      scene.add(model);
    });

    this.controls = new OrbitControls(this.camera, renderer.domElement);
    this.controls.enableDamping = true;
    this.controls.dampingFactor = 0.05;
    this.controls.screenSpacePanning = false;
    this.controls.maxPolarAngle = Math.PI / 2;

    const animate = () => {
      requestAnimationFrame(animate);
      this.controls.update();
      renderer.render(scene, this.camera);
    };

    animate();
  }

}
